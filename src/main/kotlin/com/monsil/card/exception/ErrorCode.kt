package com.moimpay.web.exception

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val detail: String
) {
    NOT_FOUND_FILE(HttpStatus.NO_CONTENT, "파일이 비었습니다."),

    /* 400 BAD_REQUEST */
    /* 공통 */
    /* 매우 중요, 회원권한자체가 없음 -> 강제로그아웃 요망 */
    BLOCK_USER(HttpStatus.UNAUTHORIZED, "더 이상 앱 사용이 불가능한 사용자입니다."),
    NOT_AVAILABLE_GOSIWON(HttpStatus.UNAUTHORIZED, "더 이상 이용이 불가능한 고시원입니다. 자세한 내용은 관리자 측으로 문의바랍니다."),
    NOT_ALLOWED_FOR_USER(HttpStatus.UNAUTHORIZED, "허가 되지 않은 사용자의 요청입니다."),

    INVALID_PARAM(HttpStatus.BAD_REQUEST, "파라미터가 유효하지 않습니다."),
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, "검색 결과가 없습니다."),
    NOT_EXIST_USER(
        HttpStatus.BAD_REQUEST,
        "아이디와 비밀번호를 확인바랍니다."
    ), // NOT_EXIST, MISSMATCH 동일한 문구 표시("존재하지 않는 유저이거나, 사용이 불가능한 유저입니다. 다시 조회 바랍니다.")
    FAILD_UPLOAD_FilE(HttpStatus.BAD_REQUEST, "파일업로드에 실패하였습니다."),


    /* 관리자 추가/등록/삭제 */
    INVALID_REMOVE_REQUEST(HttpStatus.BAD_REQUEST, "삭제할 운영자가 존재하지 않습니다. 새로고침 후 다시 요청을 시도해보세요"),
    INVALID_ADD_REQUEST(HttpStatus.BAD_REQUEST, "운영자로 등록할 수 없는 유저 입니다. 자세한 내용은 관리자에게 문의바랍니다."),
    DUPLICATE_DATA(HttpStatus.BAD_REQUEST, "이미 운영자로 등록한 유저 입니다."), // 이미 관리자로 등록했음에도 불구하고 다시 검색 요청했을 경우 응답에러
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "본인은 운영자로 등록 불가합니다."),
    INVALID_SEARCH_REQUEST(
        HttpStatus.BAD_REQUEST,
        "운영자로 추가할 수 없는 유저입니다."
    ), // 탈퇴한 유저, 이미 다른 호스트가 운영자로 등록한 유저, 호스트를 검색 하였을 경우

    /* 회원 관련 */
    NOT_EQUAL_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다. 다시 입력 바랍니다."), // 비밀번호 찾기(변경)시 서로 다른 비밀번호입력
    FAIL_SIGNGUP(HttpStatus.BAD_REQUEST, "가입에 실패하였습니다. 자세한 문의는 관리를 통해 하시기 바랍니다."), // 가입실패
    ALREADY_ENROLLED_USER(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다. 이메일 형식을 확인해주세요."),   // 중복회원가입
    ALREADY_ENROLLED_PHONE(HttpStatus.NOT_ACCEPTABLE, "이미 사용중인 휴대폰번호입니다. 다른번호로인증해주세요"), // 중복회원가입
    VERTIFY_FAIL(HttpStatus.BAD_REQUEST, "인증에 실패하였습니다."), // 본인인증에 실패하였습니다.
    NOT_EQUL_TARGET_AND_USER(HttpStatus.BAD_REQUEST, "로그인된 아이디와 수정하려는 아이디가 일치 하지 않습니다."), // 정보수정을 요청한 uid와 변경대상 ui가 불일치

    LOGIN_PARAM_FAIL(HttpStatus.BAD_REQUEST, "일치하는 회원 정보가 없습니다."),
    ALREADY_APPLIED_HOST_MSG(HttpStatus.BAD_REQUEST, "이미 호스트를 신청하셨습니다."),
    ANOTHER_DEVICE(HttpStatus.BAD_REQUEST, "기기가 변경되었습니다. 로그인하기위해선 인증이 필요합니다."), // imei 변경(기기변경)
    CHECK_ID_AND_PASSSWORD(HttpStatus.BAD_REQUEST, "아이디와 비밀번호를 확인바랍니다."), // 존재하지 않은 이메일 또는 비밀번호 실패시
    INVAILD_APPROCH(HttpStatus.BAD_REQUEST, "잘못된 접근입니다. 관리자에게 문의 바랍니다."), // 존재하지 않은 사용자로 권한인 필요한 자원을 요청하는 경우
    ALREADY_EXIST_SECONDPWD(HttpStatus.BAD_REQUEST, "이미 2차 비밀번호가 존재합니다."), // 이미 투야 홈 아이디 가입되어 있는데 또 가입요청을 시도하는 경우
    CHECK_YOUR_ROLE(HttpStatus.BAD_REQUEST, "권한이 없습니다."),
    DELETED_ARTICLE(HttpStatus.BAD_REQUEST, "삭제된 글입니다.."),
    CHECK_YOUR_DUES(HttpStatus.BAD_REQUEST, "회비추가는 10개까지만 가능합니다."),
    CHECK_YOUR_JB(HttpStatus.UNAUTHORIZED, "중복 회비가있습니다. 확인해주세요"),


    /* 결제 관련 */
    NOT_FOUND_CI(HttpStatus.BAD_REQUEST, "본인인증이 필요합니다."),
    NOT_FOUND_ROOM(
        HttpStatus.BAD_REQUEST,
        "결제하실 수 없는 호실입니다. 다시 접속 바랍니다."
    ), // 결제 시점에 존재하지 않는 호실( 방,고시원 보여주기 OFF, 승인 OFF, 삭제 ON) , 이미 다른 유저가 결제중 일 경우에도 동일함
    PAY_PROCESS_DENIED(HttpStatus.BAD_REQUEST, "결제가 진행 중인 호실입니다. 추후에 다시 이용바랍니다."),
    ALREADY_STAYED_ROOM(HttpStatus.BAD_REQUEST, "이미 투숙중인 방이 존재하므로 추가적인 호실 결제가 불가능합니다."), // 이미 투숙중인 호실인 경우 예외
    ALREADY_OTHER_GUEST(HttpStatus.BAD_REQUEST, "이미 다른 게스트가 사용중입니다. 다른 호실을 이용합니다."), // 다른 유저가 사용하고 있는 호실인 경우
    INSUFFICIENT_BLANCE(HttpStatus.BAD_REQUEST, "보유 포인트가 부족합니다."), // 보유 포인트보다 사용금액이 큽니다.
    DIFFERENT_PRICE_REQUEST(
        HttpStatus.BAD_REQUEST,
        "호스트의 호실 가격변경으로 현재 지불하려고 하는 결제금액과 맞지 않습니다. 재접속하여 결제바랍니다."
    ), // 불일치 가격정보
    NOT_ALLOWED_EXTENSTION(HttpStatus.BAD_REQUEST, "만료일 기준 7일전부터 연장이 가능합니다."), // 호실 연장기준 불충족
    NOT_AVAILABLE_DATE(
        HttpStatus.BAD_REQUEST,
        "투숙 시작일을 다시 선택하세요. 투숙 시작일은 기존 호실의 투숙이 종료되는 시작 +1일이어야 합니다."
    ), // (연장) 중복된 투숙일 선택


    /* 401 Unauthorized */
    EMPTY_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 없습니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다."),
    MISMATCH_RSA_KEY(HttpStatus.UNAUTHORIZED, "RSA 키가 일치하지 않습니다."),
    DONT_MATCH_CI(HttpStatus.UNAUTHORIZED, "본인인증 정보가 계정정보와 일치 하지 않습니다."),

    /* 호스트 룸 등록 */
    NOT_EDIT_FOR_ROOM(HttpStatus.BAD_REQUEST, "현재 투숙객이 존재하는 호실에 대해서는 공개 또는 타임세일을 등록하실 수 없습니다."),
    NOT_REGISTERED_ROOM_FOR_OVERLAP_SHOECLOST(HttpStatus.BAD_REQUEST, "이미 등록되어 있는 신발장은 등록하실 수 없습니다. 다시 시도하시기 바랍니다."),
    NOT_REGISTERED_ROOM_FOR_OVERLAP_UNIT(HttpStatus.BAD_REQUEST, "이미 등록되어 있는 호실은 등록하실 수 없습니다. 다시 시도하시기 바랍니다."),
    NOT_REMOVE_ROOM_ALREADY_STAYED_ROOM(HttpStatus.BAD_REQUEST, "예약 정보가 존재하므로 룸 삭제가 불가능합니다."),

    /* 호스트 결제 승인 요청 */
    NOT_EXIST_PAY_RESERVATION(HttpStatus.BAD_REQUEST, "관련한 결제 내역이 존재하지 않아 승인 요청을 보낼 수 없습니다. 관라지에게 문의 바랍니다."),
    NOT_ALLOWED_PAY_APPROVAL(HttpStatus.BAD_REQUEST, "승인요청을 보낼 수 없는 호실입니다. 호스트측에서 다시 한번 확인 부탁드립니다."),

    /* 호스트 키발급 요청 */
    ALREADY_KEY_EXIST(HttpStatus.BAD_REQUEST, "이미 키가 발급된 상태입니다. 더 이상 키를 발급할 수 없습니다."),
    NOT_EXIST_DEVICE(HttpStatus.BAD_REQUEST, "키 발급에 필요한 기기가 등록되어 있지 않습니다. 기기 등록 후 사용바랍니다."),
    NOT_EXIST_PAY_RESERVATION_FOR_KEY(HttpStatus.BAD_REQUEST, "키발급에 필요한 결제정보가 존재하지 않습니다. 아이호미 관리자측으로 문의바랍니다."),
    GOSIWON_NOT_ALLOWED_SMART_KEY(HttpStatus.BAD_REQUEST, "해당 고시원은 스마트키를 사용하지 않는 고시원입니다. 설정을 다시 해주시기 바랍니다."),
    BEFORE_PAY_APPROVAL(HttpStatus.BAD_REQUEST, "결제 승인 전에는 키를 발급할 수 없습니다."),

    /* 호스트 키발급/결제 승인 공통 */
    NOT_EXIST_ROOM(HttpStatus.BAD_REQUEST, "존재하지 않는 호실입니다. 다시 확인 바랍니다."),
    NOT_ALLOWED(HttpStatus.BAD_REQUEST, "허가되지 않은 유저의 요청입니다."), // 고시원 등록
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버측에러 발생, 아이호미 관리자측으로 연락 바랍니다."),

    /* 호스트 기기 등록요청 */
    ALREADY_DEVICE_EXIST(HttpStatus.BAD_REQUEST, "이미 해당 고시원에 등록된 기기가 존재합니다. 기존의 기기를 삭제한 후 재 등록 바랍니다."),
    OVERRAP_MACADDRESS_FROM_MY(HttpStatus.BAD_REQUEST, "이미 현재 고시원에서 등록된 장치입니다."),
    OVERRAP_MACADDRESS_FROM_OTHER(HttpStatus.BAD_REQUEST, "다른 고시원에서 등록된 장치입니다."),
    NOT_SMART_GOSIWON(HttpStatus.BAD_REQUEST, "스마트 고시원이 아니면 해당 기능을 이용하실 수 없습니다"),
    NOT_SOLITY_REG_DEVICE(HttpStatus.BAD_REQUEST, "장치 등록 오류입니다. 관리자에게 문의하십시오."),

    /* 호스트 기기 삭제 */
    DELETE_FAIL_NOT_FOUND_DEVICE(HttpStatus.BAD_REQUEST, "기기가 존재하지 않습니다."),
    DELETE_FAIL_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "허가되지 않은 유저의 요청입니다."),
    DELETE_FAIL_NOT_FOUND_GOSIWON(HttpStatus.BAD_REQUEST, "존재하지 않는 고시원 입니다."),
    DELETE_FAIL(HttpStatus.BAD_REQUEST, "장치 삭제에 실패하였습니다. 다시 시도해보시거나 관리자에게 문의바랍니다"),


    /* 투야 기기 */
    NOT_EXIST_TUYA_ID(HttpStatus.BAD_REQUEST, "해당 고시원에 할당된 투야 홈 아이디가 존재하지 않습니다. 관리자에게 문의바랍니다."),

    /* 유저 키 조회 요청 */
    NOT_UNDER_CONTRACT(HttpStatus.BAD_REQUEST, "계약 중인 방이 존재하지 않습니다. 현재 계약 중인 방이 존재할 경우 사용가능합니다."),

    /* 유저/호스트 OTP 신청 */
    NOT_EXIST_KEY(HttpStatus.BAD_REQUEST, "존재하지 않는 키입니다. 재접속 해보시고 같은 문제가 생길 경우 관리자에게 문의 바랍니다."),
    NOT_ALLOWED_OTP(HttpStatus.BAD_REQUEST, "해당 기기에 잠금이 걸려있습니다. 장치를 이용하실 수 없습니다. 호스트에게 문의바랍니다."),
    THIS_IS_KEY_FOR_USER(HttpStatus.BAD_REQUEST, "호스트용으로 발급된 키를 사용하시기 바랍니다. 자세한 내용은 관리자측에 문의바랍니다."),
    SERVER_ERROR_FOR_DEVICE(HttpStatus.BAD_REQUEST, "서버측오류로 현재 기기 사용이 불가합니다. 자세한 내용은 관리자측에 문의바랍니다."),


    /* 호스트 잠금/해제 요청 */
    NOT_EXIST_FOR_LOCK_UNLOCK_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. 기기가 존재하지 않습니다. 관리자에게 문의 바랍니다."), // RE 메인화면 이동
    NOT_EXIST_GOSIWON(HttpStatus.BAD_REQUEST, "존재하지 않는 고시원입니다. 새로고침 바랍니다."), // RE 로그아웃 필요
    DUPLICATE_REQUEST(HttpStatus.BAD_REQUEST, "중복된 요청입니다."),

    /* 리뷰작성 */
    NOT_WRITE_REVIEW(
        HttpStatus.BAD_REQUEST,
        "리뷰를 작성하실 수 없습니다.결제한 방이없거나, 리뷰 작성 기한을 넘기셨습니다. (작성기한 : 입실일로부터 10 ~ 20일 사이)"
    ),    //  잘못된 리뷰 작성 요청
    NOT_WRITE_TWICE_REVIEW(
        HttpStatus.BAD_REQUEST,
        "이미 묶고 있는 고시원 방 결제 건에 대해서 리뷰를 작성하셨습니다."
    ),                                            //  하나의 결제건에 또 리뷰 작성 시도

    /* interval server error */
    LOGIN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버측 에러로 로그인이 불가합니다. 자세한 사항은 관리자에게 연락바랍니다."),  // 서버측에러

    /* [결제] 포인트 사용 */
    LACK_OF_POINT(
        HttpStatus.BAD_REQUEST,
        "포인트 사용에 문제가 생겼습니다. 다시 결제를 시도해보시고 문제가 계속될경우 관리자에게 연락바랍니다."
    ), // 사용하려고 하는 포인트 > 가지고 있는 포인트
    NOT_ALLOEWD_POINT(HttpStatus.BAD_REQUEST, "역경매중인 호실의 경우 포인트를 사용하실 수 없습니다."), // 역경매 중인 방에 대해서는 포인트 사용 불가

    /* 무조건 강제로 사용자 로그아웃 강제 이동 */
    NOT_AVAILABLE_USER(HttpStatus.UNAUTHORIZED, "존재하지 않는 유저 혹은 서비스 사용이 불가능 한 유저입니다."),


    /* 유저 고시원 QNA 게시글 작성 */
    NOT_WRITE_QNA(HttpStatus.BAD_REQUEST, "투숙 중인 고시원에만 QNA 글을 작성하실 수 있습니다."),


    /* 게시판 본인 글이 아닌 경우 접근*/
    NOT_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    ALREADY_ANSWERED(HttpStatus.BAD_REQUEST, "이미 답변 완료된 글입니다."),
    NOT_EXIST_NOTICE(HttpStatus.BAD_REQUEST, "존재하지 않는 글입니다"),

    /* RF 카드 발급 */
    NOT_EXISTED_RESERVATION(HttpStatus.BAD_REQUEST, "해당 예약정보로 RF 카드를 발급하실 수 없습니다.(존재하지 않는 예약정보)"),


    /* 기기 찾기 실패 */
    NOT_FOUND_DEVICE(HttpStatus.BAD_REQUEST, "기기를 찾을 수 없습니다."),


    /* 도어락 비밀번호 목록, 상세, 별칭 등록, 삭제 (PW 관리) */
    INVALID_PW_OPERATION(HttpStatus.BAD_REQUEST, "도어락 비밀번호 동작이 되지 않습니다. 토큰 또는 장치 번호를 확인하세요."),
    INVALID_PW_ADDR_REQUEST(HttpStatus.BAD_REQUEST, "도어락 비밀번호 인덱스가 잘못되었습니다. 비밀번호는 1~4번까지 등록할 수 있습니다."),


    /* 카드 목록, 별칭 등록, 삭제 (Card 관리) */
    INVALID_CARD_OPERATION(HttpStatus.BAD_REQUEST, "카드 동작이 되지 않습니다. 토큰 또는 장치 번호를 확인하세요."),
    INVALID_CARD_ADDR_REQUEST(HttpStatus.BAD_REQUEST, "카드 인덱스가 잘못되었습니다. 카드는 1~100번까지 등록할 수 있습니다."),

    /* 로비 등록 오류 */
    INVALID_UPDATE_OPERATION(HttpStatus.BAD_REQUEST, "데이터를 수정할 수 없습니다."),

    /* 샘플 에러 코드입니다. 지우지 마세요. 새로 추가되는 항목은 윗줄에 기록해 주세요. */
    SAMPLE_ERROR_CODE_DONT_DELETE(HttpStatus.BAD_REQUEST, "샘플 에러 코드입니다."),

    /*고유번호가 이미 존재*/
    ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "고유번호가 이미 존재 합니다. 운영자에게 문의 하세요"),

    /*404*/
    NOT_FOUND_PAGE(HttpStatus.NOT_FOUND, "페이지가 없습니다.")

}

fun ErrorCode.response(data: HashMap<String, Any>): Mono<ServerResponse> {
    return ServerResponse.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
        .bodyValue(mapOf("error" to name, "result" to data))
}

fun ErrorCode.errorResponse(): Mono<ServerResponse> {
    return ServerResponse.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
        .bodyValue(mapOf("error" to name, "message" to detail))
}