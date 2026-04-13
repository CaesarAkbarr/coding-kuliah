import cv2
import mediapipe as mp
import pyautogui
import math

# 1. Inisialisasi MediaPipe & Kamera
mp_hands = mp.solutions.hands
hands = mp_hands.Hands(max_num_hands=1, min_detection_confidence=0.8)  # Tingkatkan confidence untuk lebih cepat
mp_draw = mp.solutions.drawing_utils
cap = cv2.VideoCapture(0)
cap.set(3, 160)
cap.set(4, 120)
cap.set(5, 15)

# 2. Setup Layar & Smoothing
screen_w, screen_h = pyautogui.size()
plocX, plocY = 0, 0 # Previous location
clocX, clocY = 0, 0 # Current location
alpha = 0.2         # Faktor smoothing (Alpha) yang kita bahas / sensitivitas
is_clicking = False # State flag buat klik kiri
frame_count = 0 # FPS kamera

while cap.isOpened():
    success, img = cap.read()
    if not success: break

    # Flip gambar biar kayak cermin
    img = cv2.flip(img, 1)
    h, w, c = img.shape
    img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    # Di dalam loop while, sebelum hands.process
    img_small = cv2.resize(img, (160, 120))  # Kurangi resolusi untuk performa lebih baik
    img_rgb = cv2.cvtColor(img_small, cv2.COLOR_BGR2RGB)
    
    # Skip frames untuk optimasi: proses setiap 2 frame sekali
    if frame_count % 2 == 0:
        results = hands.process(img_rgb)
    else:
        results = None  # Pastikan results None jika skip
    frame_count += 1

    if results and results.multi_hand_landmarks:
        for hand_landmarks in results.multi_hand_landmarks:
            # Ambil koordinat Ujung Telunjuk (Index 8) dan Jari Tengah (Index 12)
            landmarks = hand_landmarks.landmark
            
            # Koordinat Telunjuk (8) untuk gerakin kursor
            tx, ty = landmarks[8].x, landmarks[8].y
            
            # 3. Mapping ke resolusi layar
            # Kita pakai nilai normalized (0-1) langsung dikali resolusi monitor
            targetX = tx * screen_w
            targetY = ty * screen_h
            
            # 4. Logika Smoothing (Weighted Average)
            clocX = (alpha * targetX) + ((1 - alpha) * plocX)
            clocY = (alpha * targetY) + ((1 - alpha) * plocY)
            
            pyautogui.moveTo(clocX, clocY)
            plocX, plocY = clocX, clocY

            # 5. Logika Klik Kiri (Telunjuk vs Jari Tengah)
            # Hitung jarak antara Ujung Telunjuk (8) dan Ujung Jari Tengah (12)
            dx = landmarks[8].x - landmarks[12].x
            dy = landmarks[8].y - landmarks[12].y
            distance = math.sqrt(dx**2 + dy**2)

            threshold = 0.05 # Ambang batas jarak merapat
            
            if distance < threshold and not is_clicking:
                pyautogui.mouseDown()
                is_clicking = True
                print("Klik Kiri! 🖱️")
            elif distance > threshold:
                pyautogui.mouseUp()
                print("Not Klik Kiri! 🖱️")
                is_clicking = False

            # Gambar landmark di layar buat debugging
            mp_draw.draw_landmarks(img, hand_landmarks, mp_hands.HAND_CONNECTIONS)

    cv2.imshow("Hand Scanner Mouse", img)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()