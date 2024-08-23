document.getElementById('file').addEventListener('change', function (event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    if (file) {
        document.getElementById('selectFileButton').style.display = 'none';
        const uploadButton = document.getElementById('uploadButton');
        const modifyButton = document.getElementById('modifyButton');
        if (uploadButton) {
            uploadButton.style.display = 'block';
        }
        if (modifyButton) {
            modifyButton.style.display = 'block';
        }
    }

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

    setStartSpinner()

    reader.onloadend = function () {
        const base64String = reader.result.replace("data:", "").replace(/^.+,/, "");
        console.log("test");
        console.log(base64String);

        const data = {
            file: base64String
        };

        const buttonId = event.submitter.id;
        let apiUrl = '';
        if (buttonId === 'uploadButton') {
            apiUrl = '/api/manager/uploadMain';
        } else if (buttonId === 'modifyButton') {
            apiUrl = '/api/manager/modifyMain';
        }

        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        return response.text().then(text => {
                            throw new Error(`서버 응답 오류: ${text}`);
                        });
                    }
                })
                .then(data => {
                if (data.url) {
                    document.getElementById('message').innerText = "업로드 성공!";
                    document.getElementById('previewImage').src = data.url;
                    document.getElementById('noImage').style.display = 'none';
                    loadCurrentImages();
                    location.reload();
                    alert("업로드 성공")
                } else {
                    document.getElementById('message').innerText = `업로드 실패: ${data.error}`;
                }
            })
            .catch(error => {
                document.getElementById('message').innerText = `업로드 실패: ${error}`;
            })
            .finally(() => {
                setStopSpinner()
            });
    };

    reader.readAsDataURL(file);
});

function loadCurrentImages() {
    const currentImagesDiv = document.getElementById('currentImages');
    if (!currentImagesDiv) {
        console.error('Element with ID "currentImages" not found.');
        return;
    }

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
                        `;
                }
            })
            .catch(error => {
                currentImagesDiv.innerHTML = `
                        <div class="no-image">
                            <p>업로드된 이미지가 없습니다.</p>
                    `;
            });
    } else {
        currentImagesDiv.innerHTML = `
                <div class="no-image">
                    <p>업로드된 이미지가 없습니다.</p>
        `;
    }
}

window.onload = loadCurrentImages;


