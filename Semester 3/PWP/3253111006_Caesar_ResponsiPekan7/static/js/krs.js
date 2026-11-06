$(document).ready(function () {
  // Fungsi menampilkan notifikasi Bootstrap secara dinamis
  function showAlert(message, type) {
    const icon =
      type === "success"
        ? "bi-check-circle-fill"
        : "bi-exclamation-triangle-fill";
    const alertHtml = `
            <div class="alert alert-${type} alert-dismissible fade show d-flex align-items-center shadow-sm" role="alert">
                <i class="bi ${icon} me-2 fs-5"></i>
                <div class="flex-grow-1">${message}</div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
    $("#alert-container").html(alertHtml);

    // Auto dismiss setelah 4 detik
    setTimeout(function () {
      $(".alert").alert("close");
    }, 4000);
  }

  // 1. Integrasi AJAX: Ambil Mata Kuliah
  $(document).on("click", ".btn-ambil", function () {
    const btn = $(this);
    const mkId = btn.data("id");
    const mkNama = btn.data("nama");

    btn
      .prop("disabled", true)
      .html(
        '<span class="spinner-border spinner-border-sm me-1"></span>Proses...',
      );

    $.ajax({
      url: "/api/krs/ambil",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({ matakuliah_id: mkId }),
      success: function (response) {
        // Update total SKS di badge antarmuka
        $("#badge-total-sks").text(response.total_sks);

        // Ubah tampilan tombol menjadi tombol Batal
        btn
          .removeClass("btn-primary btn-ambil")
          .addClass("btn-outline-danger btn-batal")
          .prop("disabled", false)
          .html('<i class="bi bi-trash3 me-1"></i> Batalkan');

        showAlert(response.message, "success");
      },
      error: function (xhr) {
        btn
          .prop("disabled", false)
          .html('<i class="bi bi-plus-circle me-1"></i> Ambil MK');
        const err = xhr.responseJSON
          ? xhr.responseJSON.message
          : "Gagal mengambil mata kuliah.";
        showAlert(err, "danger");
      },
    });
  });

  // 2. Integrasi AJAX: Batalkan Mata Kuliah
  $(document).on("click", ".btn-batal", function () {
    const btn = $(this);
    const mkId = btn.data("id");
    const mkNama = btn.data("nama");

    btn
      .prop("disabled", true)
      .html(
        '<span class="spinner-border spinner-border-sm me-1"></span>Proses...',
      );

    $.ajax({
      url: "/api/krs/batal",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({ matakuliah_id: mkId }),
      success: function (response) {
        // Update total SKS di badge antarmuka
        $("#badge-total-sks").text(response.total_sks);

        // Ubah tampilan tombol kembali menjadi tombol Ambil
        btn
          .removeClass("btn-outline-danger btn-batal")
          .addClass("btn-primary btn-ambil")
          .prop("disabled", false)
          .html('<i class="bi bi-plus-circle me-1"></i> Ambil MK');

        showAlert(response.message, "warning");
      },
      error: function (xhr) {
        btn
          .prop("disabled", false)
          .html('<i class="bi bi-trash3 me-1"></i> Batalkan');
        const err = xhr.responseJSON
          ? xhr.responseJSON.message
          : "Gagal membatalkan mata kuliah.";
        showAlert(err, "danger");
      },
    });
  });

  // 3. Integrasi jQuery DOM: Live Filter / Pencarian Mata Kuliah
  $("#search-mk").on("keyup", function () {
    const keyword = $(this).val().toLowerCase();
    $("#table-matakuliah tbody tr").filter(function () {
      $(this).toggle($(this).text().toLowerCase().indexOf(keyword) > -1);
    });
  });
});

// Fungsi Global Menampilkan Pop-Up Modal Error
window.showPopupModal = function (title, heading, message) {
  $("#modalTitle").text(title);
  $("#modalHeading").text(heading);
  $("#modalBody").text(message);
  const modalEl = document.getElementById("feedbackModal");
  if (modalEl) {
    const feedbackModal = new bootstrap.Modal(modalEl);
    feedbackModal.show();
  }
};

$(document).ready(function () {
  // 1. Validasi Client-Side Form Login (Cegah tipe data salah sebelum dikirim)
  $("#form-login").on("submit", function (e) {
    const nim = $("#nim").val().trim();
    if (!/^\d+$/.test(nim)) {
      e.preventDefault();
      showPopupModal(
        "Tipe Data Salah",
        "Format NIM Tidak Valid",
        "NIM harus berupa angka (numerik), tidak boleh mengandung huruf atau simbol!",
      );
    }
  });

  // 2. Validasi Client-Side Form Register
  $("#form-register").on("submit", function (e) {
    const nim = $("#nim").val().trim();
    const pass = $("#password").val();

    if (!/^\d+$/.test(nim)) {
      e.preventDefault();
      showPopupModal(
        "Tipe Data Salah",
        "Format NIM Tidak Valid",
        "NIM harus berupa angka (numerik), tidak boleh mengandung huruf atau simbol!",
      );
      return;
    }

    if (nim.length < 8 || nim.length > 15) {
      e.preventDefault();
      showPopupModal(
        "Panjang NIM Tidak Sesuai",
        "Panjang Karakter Tidak Valid",
        "NIM harus terdiri dari 8 hingga 15 digit angka!",
      );
      return;
    }

    if (pass.length < 6) {
      e.preventDefault();
      showPopupModal(
        "Kata Sandi Lemah",
        "Password Terlalu Pendek",
        "Kata sandi minimal harus 6 karakter untuk menjaga keamanan akun!",
      );
      return;
    }
  });
});
