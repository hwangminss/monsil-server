document.getElementById('file').addEventListener('change', function (event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
        document.getElementById('previewImage').src = e.target.result;
        document.getElementById('noImage').style.display = 'none';
        document.getElementById('previewImage').style.display = 'block';
    };

    reader.readAsDataURL(file);
});

document.getElementById('uploadForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const fileInput = document.getElementById('file');
    const file = fileInput.files[0];
    const reader = new FileReader();

    reader.onloadend = function () {
        const base64String = reader.result.replace("data:", "").replace(/^.+,/, "");

        const data = {
            name: document.getElementById('name').value,
            file: base64String
        };

        fetch('/api/manager/uploadMain', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                if (data.url) {
                    document.getElementById('message').innerText = "업로드 성공!";
                    document.getElementById('previewImage').src = data.url;
                    document.getElementById('noImage').style.display = 'none';
                    location.reload();
                    loadCurrentImages();
                } else {
                    document.getElementById('message').innerText = `업로드 실패: ${data.error}`;
                }
            })
            .catch(error => {
                document.getElementById('message').innerText = `업로드 실패: ${error}`;
            });
    };

    reader.readAsDataURL(file);
});

function loadCurrentImages() {
    const currentImagesDiv = document.getElementById('currentImages');
    const imagePath = currentImagesDiv.getAttribute('data-image-url');

    if (imagePath) {
        fetch(imagePath)
            .then(response => {
                if (response.ok) {
                    const img = document.createElement('img');
                    img.src = imagePath;
                    currentImagesDiv.innerHTML = '';
                    currentImagesDiv.appendChild(img);
                } else {
                    currentImagesDiv.innerHTML = `
                            <div class="no-image">
                                <p>업로드된 이미지가 없습니다.</p>
                            </div>
                        `;
                }
            })
            .catch(error => {
                currentImagesDiv.innerHTML = `
                        <div class="no-image">
                            <p>업로드된 이미지가 없습니다.</p>
                        </div>
                    `;
            });
    } else {
        currentImagesDiv.innerHTML = `
                <div class="no-image">
                    <p>업로드된 이미지가 없습니다.</p>
                </div>
            `;
    }
}

window.onload = loadCurrentImages;