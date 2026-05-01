import cv2

print("OpenCV berhasil diimpor!")
print("Versi OpenCV:", cv2.__version__)

# Menginisialisasi webcam (0 biasanya index untuk kamera internal)
cap = cv2.VideoCapture(0)

while cap.isOpened():
    # Membaca frame demi frame dari kamera
    success, image = cap.read()
    if not success:
        break

    # Menampilkan hasil tangkapan kamera ke jendela
    cv2.imshow('Hand Scanner Test', image)

    # Berhenti jika menekan tombol 'q'
    if cv2.waitKey(5) & 0xFF == ord('q'):
        print("Kamera ditutup")
        break

cap.release()
cv2.destroyAllWindows()