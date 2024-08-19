document.addEventListener('DOMContentLoaded', function() {
    let startY;
    let isDragging = false;
    let container = document.querySelector('.container');
    let section2 = document.querySelector('.section2');

    if (!section2) {
        console.error('Section 2 element not found');
        return;
    }

    function fadeOutAndMoveUp() {
        container.classList.add('fade-out');
        container.classList.remove('show');
    }

    function fadeInAndMoveDown() {
        container.classList.remove('fade-out');
        container.classList.add('show');
    }

    function isNearTopOfSection2() {
        let rect = section2.getBoundingClientRect();
        console.log(rect.top);
        return rect.top >= 0;
    }

    document.addEventListener('touchstart', function(event) {
        startY = event.touches[0].clientY;
    }, { passive: true });

    document.addEventListener('touchmove', function(event) {
        let currentY = event.touches[0].clientY;

        if (startY && startY - currentY > 1) {
            fadeOutAndMoveUp();
        } else if (currentY - startY > 1 && isNearTopOfSection2()) {
            fadeInAndMoveDown();
        }
    }, { passive: true });

    document.addEventListener('touchend', function(event) {
        startY = null;
    }, { passive: true });

    document.addEventListener('mousedown', function(event) {
        startY = event.clientY;
        isDragging = true;
    });

    document.addEventListener('mousemove', function(event) {
        if (!isDragging) return;
        let currentY = event.clientY;

        if (startY && startY - currentY > 1) {
            fadeOutAndMoveUp();
        } else if (currentY - startY > 1 && isNearTopOfSection2()) {
            fadeInAndMoveDown();
        }
    });

    document.addEventListener('mouseup', function(event) {
        isDragging = false;
        startY = null;
    });

    document.addEventListener('wheel', function(event) {
        if (event.deltaY > 1) {
            fadeOutAndMoveUp();
        } else if (event.deltaY < -1 && isNearTopOfSection2()) {
            fadeInAndMoveDown();
        }
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const targetDate = new Date('2025-11-08T13:00:00');

    function updateCountdown() {
        const now = new Date();
        const timeLeft = targetDate - now;

        if (timeLeft < 0) {
            document.getElementById('countdown-text').textContent = "은솔 ♥️ 민의 결혼식 25년 11월 8일";
            return;
        }

        const days = Math.floor(timeLeft / (1000 * 60 * 60 * 24));
        const hours = Math.floor((timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);

        document.getElementById('days').textContent = days;
        document.getElementById('hours').textContent = hours;
        document.getElementById('minutes').textContent = minutes;
        document.getElementById('seconds').textContent = seconds;

        const countdownText = hours <= 12
            ? "은솔 ♥️ 민<br><br>결혼식 D-DAY"
            : `은솔 ♥️ 민의 결혼식이 <span class="highlight">${days + 1}일</span> 남았습니다.`;

        document.getElementById('countdown-text').innerHTML = countdownText;
    }

    setInterval(updateCountdown, 1000);
    updateCountdown();
});
