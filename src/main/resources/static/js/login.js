document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('loginForm');
    const errorMessage = document.getElementById('error-message');

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = new FormData(form);
        const data = {
            name: formData.get('name'),
            password: formData.get('password')
        };

        fetch('/api/manager/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                window.location.href = '/manager';
            } else {
                errorMessage.textContent = result.message || '로그인에 실패했습니다. 다시 시도해 주세요.';
                errorMessage.style.display = 'block';
            }
        })
        .catch(error => {
            console.error('로그인 요청 중 오류 발생:', error);
            errorMessage.textContent = '로그인 요청 중 오류가 발생했습니다. 나중에 다시 시도해 주세요.';
            errorMessage.style.display = 'block';
        });
    });
});
