$(document).ready(function () {
    $(".edit-button").click(function () {
        var $row = $(this).closest("tr");

        var id = $(this).data("id");
        var bank = $row.find(".bank").val();
        var account = $row.find(".account").val();
        var kakao = $row.find(".kakao").val();
        var phone = $row.find(".phone").val();

        setStartSpinner()
        $.ajax({
            url: '/api/family/update',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                id: id,
                bank: bank,
                account: account,
                kakao: kakao,
                phone: "tel:" + phone,
                message: "sms:" + phone
            }),
            success: function (response) {
                alert('수정이 완료되었습니다.');
                console.log('서버 응답:', response);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('서버 오류 발생: ' + textStatus);
                console.log('서버 오류:', errorThrown);
            },
            complete: function () {
                setStopSpinner()
            }
        });
    });

    $(".delete-button").click(function () {
        var $row = $(this).closest("tr");
        var id = $(this).data("id");

        setStartSpinner();

        $.ajax({
            url: '/api/family/delete/' + id,
            method: 'DELETE',
            contentType: 'application/json',
            success: function (response) {
                alert('삭제가 완료되었습니다.');
                console.log('서버 응답:', response);

                // 삭제된 행을 DOM에서 제거
                $row.remove();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('서버 오류 발생: ' + textStatus);
                console.log('서버 오류:', errorThrown);
            },
            complete: function () {
                setStopSpinner();
            }
        });
    });
});

// 모달 열기 및 닫기
const modal = document.getElementById("myModal");
const btn = document.getElementById("openModalBtn");
const span = document.getElementsByClassName("close")[0];

btn.onclick = function() {
    modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

$(".add-button").click(function () {
    const role = parseInt($("#role").val());
    const name = $("#name").val();
    const bank = $("#bank").val();
    const account = $("#account").val();
    const kakao = $("#kakao").val();
    const phone = $("#phone").val().replace(/\D/g, '');
    const side = $("#side").val() === "groom";

    // JSON 데이터 객체 생성
    const data = {
        role: role, // 이미 숫자로 변환됨
        name: name,
        bank: bank,
        account: account,
        kakao: kakao,
        phone: "tel:" + phone,
        message: "sms:" + phone,
        groombride: side
    };

    $.ajax({
        url: '/api/family/add',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            console.log('성공:', response);
            alert('추가완료');
            modal.style.display = "none";
        },
        error: function(xhr, status, error) {
            console.error('실패:', error);
            alert('정보 전송에 실패했습니다.');
        }
    });
});
