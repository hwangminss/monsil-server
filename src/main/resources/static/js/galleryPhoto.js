document.getElementById('file').addEventListener('change', function (event) {
    const files = event.target.files;

    if (files.length > maxFiles) {
        alert(`최대 ${maxFiles}개의 이미지만 선택할 수 있습니다.`);
        document.getElementById('file').value = '';
        return;
    }

    const previewContainer = document.getElementById('previewContainer');
    previewContainer.innerHTML = '';

    if (files.length > 0) {
        document.getElementById('selectFileButton').style.display = 'none';
        document.getElementById('addButton').style.display = 'block';

        Array.from(files).forEach(file => {
            const reader = new FileReader();
            const img = document.createElement('img');

            reader.onload = function (e) {
                img.src = e.target.result;
                img.style.maxWidth = '95%';
                img.style.margin = '10px';
                previewContainer.appendChild(img);
            };

            reader.readAsDataURL(file);
        });

        document.getElementById('noImage').style.display = 'none';
        previewContainer.style.display = 'block';
    }
});

document.getElementById('addImageForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const fileInput = document.getElementById('file');
    const files = fileInput.files;

    if (files.length === 0) {
        document.getElementById('message').innerText = '파일을 선택해주세요.';
        return;
    }

    setStartSpinner();

    // 여러 개의 파일을 비동기적으로 처리
    Promise.all(Array.from(files).map(file => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();

            reader.onloadend = function () {
                const base64String = reader.result.replace("data:", "").replace(/^.+,/, "");
                const data = {
                    file: base64String,
                    name: file.name
                };

                fetch('/api/manager/uploadGallery', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.url) {
                        resolve('성공');
                    } else {
                        reject(`추가 실패: ${data.error}`);
                    }
                })
                .catch(error => {
                    reject(`추가 실패: ${error}`);
                });
            };

            reader.readAsDataURL(file);
        });
    }))
    .then(() => {
        document.getElementById('message').innerText = '모든 이미지가 추가되었습니다!';
        location.reload();
        alert("추가 완료");
    })
    .catch(error => {
        document.getElementById('message').innerText = error;
    })
    .finally(() => {
        setStopSpinner();
    });
});

function deleteImage(id) {
    if (confirm("정말로 삭제하시겠습니까?")) {
        fetch(`/api/manager/deleteGallery/${id}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                document.getElementById('message').innerText = "삭제 성공!";
                location.reload();
                alert("삭제 성공");
            } else {
                document.getElementById('message').innerText = `삭제 실패: ${data.error}`;
            }
        })
        .catch(error => {
            document.getElementById('message').innerText = `삭제 실패: ${error}`;
        });
    }
}
