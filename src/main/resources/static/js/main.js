document.addEventListener('DOMContentLoaded', function() {
    let startY;
    let isDragging = false;

    document.addEventListener('touchstart', function(event) {
        startY = event.touches[0].clientY;
    }, { passive: true });

    document.addEventListener('touchmove', function(event) {
        let currentY = event.touches[0].clientY;

        if (startY && currentY < startY + 1) {
            document.querySelector('.container').classList.add('fade-out');
        }
    }, { passive: true });

    document.addEventListener('touchend', function(event) {
        handleTouchOrMouseEnd();
    }, { passive: true });

    document.addEventListener('mousedown', function(event) {
        startY = event.clientY;
        isDragging = true;
    });

    document.addEventListener('mousemove', function(event) {
        if (!isDragging) return;
        let currentY = event.clientY;

        if (startY && currentY < startY + 1) {
            document.querySelector('.container').classList.add('fade-out');
        }
    });

    document.addEventListener('mouseup', function(event) {
        isDragging = false;
        handleTouchOrMouseEnd();
    });

    document.addEventListener('wheel', function(event) {
        if (event.deltaY < -1) {
            document.querySelector('.container').classList.add('fade-out');
            handleTouchOrMouseEnd();
        }
    });

    function handleTouchOrMouseEnd() {
        if (document.querySelector('.container').classList.contains('fade-out')) {
            setTimeout(function() {
                window.location.href = "/main";
            }, 500);
        }
    }
});
