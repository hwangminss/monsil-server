//section1~4
document.addEventListener('DOMContentLoaded', function() {
    let startY;
    let isDragging = false;
    let container = document.querySelector('.container');
    let section2 = document.querySelector('.s2');

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

//section4
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

        const countdownText = days === 0
            ? "은솔 ♥️ 민<br><br>결혼식 D-DAY"
            : `은솔 ♥️ 민의 결혼식이 <span class="highlight">${days + 1}일</span> 남았습니다.`;

        document.getElementById('countdown-text').innerHTML = countdownText;
    }

    setInterval(updateCountdown, 1000);
    updateCountdown();
});


//section5
const images = [
        '/img/background.png',
        '/img/123.png',
        '/img/images.jpeg'
    ];

let currentIndex = 0;
let startX = 0;

function showImagePreview(index) {
    currentIndex = index;
    const modal = document.getElementById('imagePreviewModal');
    const modalImage = document.getElementById('modalImage');
    modalImage.src = images[index];
    modal.style.display = 'block';

    const imageContainer = document.getElementById('modalImageContainer');
    imageContainer.addEventListener('touchstart', handleTouchStart, false);
    imageContainer.addEventListener('touchmove', handleTouchMove, false);
}

function closeImagePreview() {
    const modal = document.getElementById('imagePreviewModal');
    modal.style.display = 'none';
}

function changeImage(direction) {
    currentIndex = (currentIndex + direction + images.length) % images.length;
    const modalImage = document.getElementById('modalImage');
    modalImage.src = images[currentIndex];
}

function handleTouchStart(event) {
    startX = event.touches[0].clientX;
}

function handleTouchMove(event) {
    if (!startX) {
        return;
    }

    let endX = event.touches[0].clientX;
    let diffX = startX - endX;

    if (Math.abs(diffX) > 50) {
        if (diffX > 0) {
            changeImage(1);
        } else {
            changeImage(-1);
        }

        startX = null;
    }
}

//section6
function initMap() {
var map = new naver.maps.Map('map', {
  center: new naver.maps.LatLng(37.565455, 126.972344),
  zoom: 17,
  scrollWheel: false,
  draggable: false,
  pinchZoom: false,
  disableDoubleClickZoom: true
});

var marker = new naver.maps.Marker({
  position: new naver.maps.LatLng(37.565495, 126.972394),
  map: map
});
}

window.onload = function() {
initMap();
};

//section7
document.addEventListener('DOMContentLoaded', function () {
    fetchFamilyEntries();
});

async function fetchFamilyEntries() {
    try {
        const response = await fetch('../api/family/list');
        const data = await response.json();

        const groomList = data.result.filter(entry => !entry.groombride);
        const brideList = data.result.filter(entry => entry.groombride);

        renderFamilyEntries(groomList, 'groom-content');
        renderFamilyEntries(brideList, 'bride-content');
    } catch (error) {
        alert('리스트를 불러오는 중 오류가 발생했습니다.');
    }
}

function renderFamilyEntries(contactInfoList, containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = contactInfoList.map(info => `
        <div class="contact-info">
            <div class="contact-info-content">
                <p><strong>${getRoleText(info.role)} ${info.name}</strong></p>
                <p>${info.bank} ${info.account}</p>
            </div>
            <div class="contact-buttons">
                <a href="tel:${info.phone}" title="Call">
                    <img src="/img/icons/call.png" alt="Call" height="24" width="24">
                </a>
                <a href="sms:${info.message}" title="SMS">
                    <img src="/img/icons/sms.png" alt="SMS" height="24" width="24">
                </a>
            </div>
        </div>
        <div class="payment-options">
            <div class="payment-option" onclick="copyAccount('${info.account}')">계좌 복사</div>
            <div class="payment-option" onclick="launchKakaoPay('${info.kakao}')">카카오페이</div>
        </div>
    `).join('');
}

function toggleExpansion(type) {
    const header = document.querySelector(`.expansion-header[onclick="toggleExpansion('${type}')"]`);
    const content = document.getElementById(`${type}-content`);

    if (content.style.display === 'none' || content.style.display === '') {
        content.style.display = 'block';
        header.classList.add('expanded');
    } else {
        content.style.display = 'none';
        header.classList.remove('expanded');
    }
}

function copyAccount(account) {
    const numbers = account.replace(/\D/g, '');
    navigator.clipboard.writeText(numbers).then(() => {
        alert('계좌 번호가 클립보드에 복사되었습니다.');
    });
}

function launchKakaoPay(kakaoUrl) {
    window.open(kakaoUrl, '_blank');
}

function getRoleText(role) {
    switch (role) {
        case 0:
            return '신랑';
        case 1:
            return '신부';
        case 2:
            return '-';
        case 3:
            return '혼주';
        default:
            return '-';
    }
}

//section8
document.addEventListener('DOMContentLoaded', function () {
    fetchGuestbookEntries();

    document.getElementById('submitButton').addEventListener('click', addGuestbookEntry);
    document.getElementById('loadMoreButton').addEventListener('click', loadMoreEntries);
    document.getElementById('showAllButton').addEventListener('click', toggleDisplayCount);
});

let displayCount = 4;
const incrementCount = 8;
const initialCount = 4;
let guestbookEntries = [];

async function fetchGuestbookEntries() {
    try {
        const response = await fetch('/api/guestbook/list');
        const data = await response.json();
        guestbookEntries = data.result;
        renderGuestbookEntries();
        updateLoadMoreVisibility();
    } catch (error) {
        alert('방명록을 불러오는 중 오류가 발생했습니다.');
    }
}

function renderGuestbookEntries() {
    const container = document.getElementById('guestbookEntries');
    container.innerHTML = guestbookEntries.slice(0, displayCount).map(entry => `
        <div class="guestbook-entry">
            <div class="guestbook-entry-header">
                <h2>${entry.name}</h2>
                <div class="guestbook-entry-actions">
                    <button onclick="editEntry(${entry.id})">
                        <img src="/img/icons/edit.png" height="24" width="24">
                    </button>
                    <button onclick="deleteEntry(${entry.id})">
                        <img src="/img/icons/delete.png" height="24" width="24">
                    </button>
                </div>
            </div>
            <p>${entry.detail}</p>
        </div>
    `).join('');
}

async function addGuestbookEntry() {
    const name = document.getElementById('name').value;
    const detail = document.getElementById('detail').value;
    const password = document.getElementById('password').value;

    if (name && detail && password) {
        const entry = { name, detail, password };
        try {
            await fetch('/api/guestbook/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(entry)
            });
            await fetchGuestbookEntries();
            document.getElementById('name').value = '';
            document.getElementById('detail').value = '';
            document.getElementById('password').value = '';
        } catch (error) {
            alert('방명록 추가 중 오류가 발생했습니다.');
        }
    } else {
        alert('모든 필드를 입력하세요.');
    }
}

function loadMoreEntries() {
    displayCount += incrementCount;
    renderGuestbookEntries();
    updateLoadMoreVisibility();
}

function toggleDisplayCount() {
    if (displayCount < guestbookEntries.length) {
        displayCount = guestbookEntries.length;
        document.getElementById('showAllButton').textContent = '줄이기';
    } else {
        displayCount = initialCount;
        document.getElementById('showAllButton').textContent = '전체보기';
    }
    renderGuestbookEntries();
    updateLoadMoreVisibility();
}

function updateLoadMoreVisibility() {
    const loadMoreButton = document.getElementById('loadMoreButton');
    const showAllButton = document.getElementById('showAllButton');

    if (displayCount >= guestbookEntries.length) {
        loadMoreButton.style.display = 'none';
        document.getElementById('showAllButton').textContent = '줄이기';
        document.getElementById('showAllButton').style.display = 'block';
    } else {
        loadMoreButton.style.display = 'block';
        document.getElementById('showAllButton').style.display = 'block';
    }
}

const apiService = {
    deleteGuestbookEntry: async function(id, password) {
        try {
            const response = await fetch('/api/guestbook/delete', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ id, password })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Network response was not ok');
            }

            const result = await response.json();
            return result;
        } catch (error) {
            console.error('Failed to delete entry:', error);
            throw error;
        }
    },

    updateGuestbookEntry: async function(updatedEntry) {
        try {
            const response = await fetch('/api/guestbook/update', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    id: updatedEntry.id,
                    name: updatedEntry.name,
                    detail: updatedEntry.detail,
                    password: updatedEntry.password
                })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Network response was not ok');
            }

            const result = await response.json();
            return result;
        } catch (error) {
            console.error('Failed to update entry:', error);
            throw error;
        }
    }
};

let currentEntryId = null;

function editEntry(id) {
    const entry = guestbookEntries.find(e => e.id === id);
    if (entry) {
        showEditDialog(entry);
    }
}

function deleteEntry(id) {
    currentEntryId = id;
    document.getElementById('deleteDialog').classList.remove('hidden');
}

function showEditDialog(entry) {
    document.getElementById('editName').value = entry.name;
    document.getElementById('editDetail').value = entry.detail;
    document.getElementById('editPassword').value = '';
    currentEntryId = entry.id;
    document.getElementById('editDialog').classList.remove('hidden');
}

function showCustomDialog(message) {
    document.getElementById('customMessage').innerText = message;
    document.getElementById('customDialog').classList.remove('hidden');
}

function closeDialog(dialogId) {
    document.getElementById(dialogId).classList.add('hidden');
}

async function confirmDelete() {
    const password = document.getElementById('deletePassword').value;
    try {
        await apiService.deleteGuestbookEntry(currentEntryId, password);
        closeDialog('deleteDialog');
        showCustomDialog('삭제되었습니다.');
        await fetchGuestbookEntries(); // Refresh entries after delete
    } catch (error) {
        showCustomDialog('비밀번호가 틀렸습니다.');
    }
}

async function confirmEdit() {
    const name = document.getElementById('editName').value;
    const detail = document.getElementById('editDetail').value;
    const password = document.getElementById('editPassword').value;

    const updatedEntry = {
        id: currentEntryId,
        name: name,
        detail: detail,
        password: password
    };

    try {
        await apiService.updateGuestbookEntry(updatedEntry);
        closeDialog('editDialog');
        showCustomDialog('수정되었습니다.');
        await fetchGuestbookEntries(); // Refresh entries after edit
    } catch (error) {
        showCustomDialog('비밀번호가 틀렸습니다.');
    }
}

function confirmCustomDialog() {
    closeDialog('customDialog');
}
