$(document).ready(function () {
    $(".edit-button").click(function () {
        var $row = $(this).closest("tr");

        var id = $row.find("td:first").text().trim();

        $.ajax({
            url: '/api/guestbook/maDelete',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                id: id,
                password: 'ghkdalsakstp'
            }),
            success: function (response) {
                alert('삭제가 완료되었습니다.');
                console.log('서버 응답:', response);
                location.reload();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('서버 오류 발생: ' + textStatus);
                console.log('서버 오류:', errorThrown);
            }
        });
    });
});
