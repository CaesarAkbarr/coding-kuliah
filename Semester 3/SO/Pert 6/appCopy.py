import threading
import time

counter = 0
lock = threading.Lock()

def tambah():
    global counter
    for _ in range(100_000):
        with lock:
            tmp = counter
            time.sleep(0)
            counter = tmp + 1

t1 = threading.Thread(target=tambah)
t2 = threading.Thread(target=tambah)
t1.start(); t2.start()
t1.join(); t2.join()

print("Hasil:", counter, "| Seharusnya: 200000")
