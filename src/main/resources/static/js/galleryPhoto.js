    document.getElementById('file').addEventListener('change', function (event) {
        const file = event.target.files[0];
        const reader = new FileReader();

        if (file) {
            document.getElementById('selectFileButton').style.display = 'none';
            document.getElementById('addButton').style.display = 'block';
        }

        reader.onload = function (e) {
            document.getElementById('previewImage').src = e.target.result;
            document.getElementById('noImage').style.display = 'none';
            document.getElementById('previewImage').style.display = 'block';
        };

        reader.readAsDataURL(file);
    });

    document.getElementById('addImageForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const fileInput = document.getElementById('file');
        const file = fileInput.files[0];
        const reader = new FileReader();

        reader.onloadend = function () {
            const base64String = reader.result.replace("data:", "").replace(/^.+,/, "");

            const data = {
                file: base64String,
                name: file.name
            };
            setStartSpinner()
            fetch('/api/manager/uploadGallery', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(data => {
                console.log(data)
                if (data.url) {
                    document.getElementById('message').innerText = "추가 성공!";
                    location.reload();
                    alert("추가완료");
                } else {
                    document.getElementById('message').innerText = `추가 실패1: ${data.error}`;
                }
            })
            .catch(error => {
                document.getElementById('message').innerText = `추가 실패2: ${error}`;
            })
            .finally(() => {
                setStopSpinner()
            });
        };

        reader.readAsDataURL(file);
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