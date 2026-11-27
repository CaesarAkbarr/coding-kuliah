$(document).ready(function() {

    // 1. Logika Tambah/Hapus Baris Key-Value
    function addKvRow(containerId, keyPlaceholder, valuePlaceholder) {
        const row = `
            <div class="kv-row">
                <input type="text" class="input-dark ${containerId === 'headers-container' ? 'header-key' : 'body-key'}" placeholder="${keyPlaceholder}">
                <input type="text" class="input-dark ${containerId === 'headers-container' ? 'header-value' : 'body-value'}" placeholder="${valuePlaceholder}">
                <button class="btn-icon btn-remove">&times;</button>
            </div>
        `;
        $(`#${containerId}`).append(row);
    }

    $('#add-header').click(function() {
        addKvRow('headers-container', 'Header Name', 'Header Value');
    });

    $('#add-body').click(function() {
        addKvRow('body-container', 'Key', 'Value');
    });

    $(document).on('click', '.btn-remove', function() {
        $(this).closest('.kv-row').remove();
    });

    // 2. Logika Tab Body (Key/Value vs Raw JSON)
    $('.tab-btn').click(function() {
        $('.tab-btn').removeClass('active');
        $(this).addClass('active');
        
        $('.tab-content').hide();
        $('#' + $(this).data('target')).show();
    });

    // 3. Logika Pencatatan Session Log
    function addLog(method, url, statusCode, statusText) {
        // Hapus teks 'Log is empty' jika ada
        if ($('.log-container p').length > 0) {
            $('.log-container').empty();
        }

        // Ambil waktu saat ini
        const now = new Date();
        const timeString = now.toLocaleTimeString('id-ID', { hour12: false });
        
        // Tentukan warna teks berdasarkan status HTTP
        const statusClass = (statusCode >= 200 && statusCode < 300) ? 'status-ok' : 'status-err';
        
        // Susun HTML log baru
        const logEntry = `
            <div class="log-item">
                <span class="log-time">[${timeString}]</span>
                <span class="log-method">${method}</span>
                <span class="log-url">${url}</span>
                <span class="${statusClass}">${statusCode} ${statusText}</span>
            </div>
        `;

        // Tambahkan di urutan teratas
        $('.log-container').prepend(logEntry);
    }

    // Fungsi Clear Log
    $('.btn-danger-outline:contains("Clear Log")').click(function() {
        $('.log-container').html('<p class="text-muted text-sm">Log is empty.</p>');
    });

    // 4. Eksekusi Request API saat tombol "Send" diklik
    $('#btn-send').click(function() {
        const method = $('#req-method').val();
        const url = $('#req-url').val();
        
        $('#response-status').text('Sending request...');
        $('#response-body').text('Loading...');
        
        // Menyusun Headers
        const requestHeaders = {};
        const token = $('#req-token').val();
        if (token) requestHeaders['Authorization'] = 'Bearer ' + token;
        
        $('.header-key').each(function(index) {
            const key = $(this).val();
            const value = $('.header-value').eq(index).val();
            if (key) requestHeaders[key] = value;
        });

        // Menyusun Body (jika metode selain GET)
        let requestBody = null;
        if (method !== 'GET') {
            const activeTab = $('.tab-btn.active').data('target');
            if (activeTab === 'body-kv') {
                requestBody = {};
                $('.body-key').each(function(index) {
                    const key = $(this).val();
                    const value = $('.body-value').eq(index).val();
                    if (key) requestBody[key] = value;
                });
                requestBody = JSON.stringify(requestBody);
            } else {
                requestBody = $('#raw-json-input').val();
            }
        }

        // AJAX Request
        $.ajax({
            url: url,
            type: method,
            headers: requestHeaders,
            data: requestBody,
            contentType: requestHeaders['Content-Type'] || 'application/json',
            success: function(response, textStatus, xhr) {
                $('#response-status').html(`<span style="color: #10b981;">Status: ${xhr.status} ${textStatus}</span>`);
                $('#response-body').text(JSON.stringify(response, null, 4));
                addLog(method, url, xhr.status, textStatus); // Catat log sukses
            },
            error: function(xhr, textStatus, errorThrown) {
                $('#response-status').html(`<span style="color: var(--danger);">Error: ${xhr.status} ${errorThrown}</span>`);
                $('#response-body').text(xhr.responseText || "No response text.");
                addLog(method, url, xhr.status, errorThrown || textStatus); // Catat log error
            }
        });
    });
});