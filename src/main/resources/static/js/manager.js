$(document).ready(function () {
    $(".edit-button").click(function () {
        var $row = $(this).closest("tr");

        var id = $(this).data("id");
        var bank = $row.find(".bank").val();
        var account = $row.find(".account").val();
        var kakao = $row.find(".kakao").val();
        var phone = $row.find(".phone").val();

        console.log("클릭된 버튼의 데이터:");
        console.log("ID:", id);
        console.log("은행명:", bank);
        console.log("계좌번호:", account);
        console.log("QR 코드:", kakao);
        console.log("전화번호 (원본):", phone);
        console.log("전화번호 (수정 후):", "tel:" + phone);

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
            }
        });
    });
});
