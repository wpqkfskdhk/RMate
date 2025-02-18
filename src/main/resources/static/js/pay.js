function pay() {
    // 아임포트 결제 객체 초기화
    let IMP = window.IMP;
    IMP.init("imp65742330");

/*    // 구매자 정보 설정
    let buyer_name = document.querySelector('#orderReceiver').textContent || "홍길동"; // 구매자 이름
    let hp = document.querySelector('#orderPhone').textContent || "010-1234-5678"; // 구매자 전화번호
    let addr = document.querySelector('#orderAddress').textContent || "Seoul, Korea"; // 구매자 주소*/

/*    // 총 결제 금액 가져오기
    let amountText = document.querySelector('#finalPrice').textContent;
    let amount = parseInt(amountText.replace(/[^0-9]/g, ''), 10); // 숫자만 추출하여 정수로 변환*/

    // 결제 요청
    IMP.request_pay({
        pg: 'kakaopay', // 결제 PG사
        pay_method: 'card', // 결제 방법
        merchant_uid: 'merchant_' + new Date().getTime(), // 고유 주문 번호
        name: "유류비 더치페이", // 결제 상품명 (예: "주문 결제")   ..
        amount: 50000, // 결제 금액 카카오맵 api를 활용해서 km단위에 특정 1400x를 곱하거나해서 값을 구하고  쿼리셀렉터로 그값을 id로 지정하여 가져온다.
/*        buyer_name: buyer_name, // 구매자 이름
        buyer_tel: hp, // 구매자 전화번호
        buyer_addr: addr, // 구매자 주소*/
    });
}
