$(document).ready(function() {
    
    // ==========================================
    // 1. DEKLARASI FUNGSI FETCH PRODUK
    // ==========================================
    function fetchProducts() {
        // Tampilkan indikator loading (berguna jika fungsi dipanggil ulang)
        $('#product-catalog').empty();
        $('#loading').show();

        $.ajax({
            url: 'https://fakestoreapi.com/products',
            method: 'GET',
            success: function(data) {
                // Acak array menggunakan algoritma sort sederhana
                let shuffled = data.sort(() => 0.5 - Math.random());
                // Ambil 8 item pertama
                let selectedProducts = shuffled.slice(0, 8);
                
                let htmlContent = '';
                
                // Looping untuk merender struktur HTML
                selectedProducts.forEach(product => {
                    // Cek state keranjang dari variabel global userCartState (dikirim dari Flask)
                    let qty = userCartState[product.id.toString()] || 0;
                    let controlsHtml = '';
                    
                    if (qty > 0) {
                        controlsHtml = `
                            <div class="input-group">
                                <button class="btn btn-outline-secondary btn-qty-minus">-</button>
                                <input type="text" class="form-control text-center input-qty" value="${qty}" readonly>
                                <button class="btn btn-outline-secondary btn-qty-plus">+</button>
                            </div>
                        `;
                    } else {
                        controlsHtml = `<button class="btn btn-outline-primary w-100 btn-add-first">Tambah ke Keranjang</button>`;
                    }
                    
                    // Sanitasi tanda kutip ganda pada title agar atribut data HTML aman
                    let safeTitle = product.title.replace(/"/g, '&quot;');
                    
                    htmlContent += `
                    <div class="col-md-3 mb-4">
                        <div class="card h-100 shadow-sm">
                            <img src="${product.image}" class="card-img-top p-4" alt="Produk" style="height: 200px; object-fit: contain;">
                            <div class="card-body d-flex flex-column">
                                <h6 class="card-title text-truncate">${product.title}</h6>
                                <p class="card-text text-success fw-bold">$${product.price}</p>
                                
                                <div class="cart-controls mt-auto" 
                                     data-id="${product.id}" 
                                     data-title="${safeTitle}" 
                                     data-price="${product.price}" 
                                     data-image="${product.image}">
                                    ${controlsHtml}
                                </div>
                            </div>
                        </div>
                    </div>
                    `;
                });
                
                // Sembunyikan loading dan tampilkan HTML hasil render
                $('#loading').fadeOut(300, function() {
                    $('#product-catalog').html(htmlContent).hide().fadeIn(400);
                });
            },
            error: function() {
                $('#loading').html('<p class="text-danger fw-bold text-center">Gagal mengambil data dari server katalog.</p>');
            }
        });
    }

    // Eksekusi fetchProducts HANYA jika berada di halaman Home (terdapat elemen #product-catalog)
    if ($('#product-catalog').length) {
        fetchProducts();
    }

    // ==========================================
    // 2. FUNGSI UPDATE KERANJANG (AJAX)
    // ==========================================
    function updateCart(container, newQty) {
        let productId = container.attr('data-id');
        
        // Gunakan fallback (|| '') agar tidak error jika atribut tidak ada di cart.html
        let productTitle = container.attr('data-title') || ''; 
        let productPrice = parseFloat(container.attr('data-price')) || 0;
        let productImage = container.attr('data-image') || '';
        
        // Disable hanya tombol plus/minus sementara server memproses
        container.find('button').prop('disabled', true);

        $.ajax({
            url: '/update_cart',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                product_id: productId,
                quantity: newQty,
                title: productTitle,
                price: productPrice,
                image: productImage
            }),
            success: function(res) {
                if (res.status === 'success') {
                    // Update Grand Total jika elemennya ada (di cart.html)
                    if ($('#cartTotal').length) {
                        $('#cartTotal').text(res.new_total.toFixed(2));
                    }
                    
                    // BUG FIX: Cek apakah userCartState terdefinisi sebelum mengubahnya.
                    // Ini mencegah JavaScript crash saat fungsi dijalankan dari cart.html
                    if (typeof userCartState !== 'undefined') {
                        userCartState[productId.toString()] = newQty;
                    }
                }
            },
            error: function() {
                alert("Gagal sinkronisasi ke server! Halaman akan dimuat ulang.");
                location.reload(); 
            },
            complete: function() {
                // Sekarang blok ini PASTI dieksekusi karena tidak ada lagi error di blok success
                container.find('button').prop('disabled', false);
            }
        });
    }

    // ==========================================
    // 3. EVENT LISTENERS
    // ==========================================
    
    // Fitur Show/Hide Password
    $(document).on('click', '.toggle-password', function() {
        let input = $(this).closest('.input-group').find('input');
        if (input.attr('type') === 'password') {
            input.attr('type', 'text');
            $(this).text('Sembunyikan');
        } else {
            input.attr('type', 'password');
            $(this).text('Lihat');
        }
    });

    // Saat klik "Tambah ke Keranjang" pertama kali
    $(document).on('click', '.btn-add-first', function() {
        let container = $(this).closest('.cart-controls');

        container.html(`
            <div class="input-group">
                <button class="btn btn-outline-secondary btn-qty-minus">-</button>
                <input type="text" class="form-control text-center input-qty" value="1" readonly>
                <button class="btn btn-outline-secondary btn-qty-plus">+</button>
            </div>
        `);

        updateCart(container, 1);
    });

    $(document).on('click', '.btn-qty-plus', function() {
        let container = $(this).closest('.cart-controls');
        let input = container.find('.input-qty');
        let currentQty = parseInt(input.val());
        let newQty = currentQty + 1;
        
        let price = parseFloat(container.attr('data-price'));
        let productId = container.attr('data-id');

        input.val(newQty);
        
        // Langsung update teks subtotal (Jika ada di halaman cart.html)
        let subtotalElem = $(`#subtotal-${productId}`);
        if (subtotalElem.length) {
            subtotalElem.text('$' + (price * newQty).toFixed(2));
        }

        updateCart(container, newQty);
    });

    $(document).on('click', '.btn-qty-minus', function() {
        let container = $(this).closest('.cart-controls');
        let input = container.find('.input-qty');
        let currentQty = parseInt(input.val());
        let newQty = currentQty - 1;
        
        let price = parseFloat(container.attr('data-price'));
        let productId = container.attr('data-id');

        if (newQty === 0) {
            // Jika dikurangi jadi 0
            let row = $(`#cart-row-${productId}`);
            if (row.length) {
                // Berada di cart.html -> Langsung hilangkan barisnya
                row.fadeOut(300, function() { 
                    $(this).remove(); 
                    if ($('#cartTable tbody tr').length === 0) {
                        $('#cartTable tbody').html('<tr id="empty-cart-row"><td colspan="5" class="text-center text-muted">Keranjang masih kosong.</td></tr>');
                    }
                });
            } else {
                // Berada di home.html -> Kembalikan ke tombol biru
                container.html(`<button class="btn btn-outline-primary w-100 btn-add-first">Tambah ke Keranjang</button>`);
            }
        } else {
            // Jika hanya dikurangi tapi belum 0 -> Langsung update angka
            input.val(newQty);
            let subtotalElem = $(`#subtotal-${productId}`);
            if (subtotalElem.length) {
                subtotalElem.text('$' + (price * newQty).toFixed(2));
            }
        }

        updateCart(container, newQty);
    });

    $('#btnRefreshCatalog').on('click', function() {
        let btn = $(this);
        let originalText = btn.html();
        
        btn.prop('disabled', true).html('Memuat...');
        fetchProducts();

        setTimeout(() => {
            btn.prop('disabled', false).html(originalText);
        }, 1000);
    });
});