# [탄다마켓 프로젝트](https://thenaeunteam.link/)

- [Android](https://play.google.com/store/apps/details?id=com.thenaeunteam.tandaowner
  )
- [iOS](https://apps.apple.com/kr/app/%ED%83%84%EB%8B%A4%EC%98%A4%EB%84%88/id1597607300)

팀명
=========
더나은팀

팀원
---------
김나은 최영진 이재현

주제
---------
편의점, 음식점 등 유통기한 임박 상품 할인 판매 또는 마감 할인 판매

## 주제 선정 이유, 기대 효과

기대 효과 : 최근 우리나라에서 쓰레기 처리 , 그 중에서도 음식물 쓰레기 처리에 대한 많은 이슈가 발생 하고 있다. 음식 낭비로 연간 20조 이상의 경제 손실이 일어나고 있으며 음식 낭비가 가정과 음식점에서가 가장
심하다. 그 중 유통기한이 지난 상품이나 당일 판매해야 할 음식들을 다 판매하지 못하고 버리게 되는 양도 많은 부분을 차지한다.

최근 온실가스의 주범으로 음식물 쓰레기가 주목 받고 있다.

우리나라는 하루 평균 약 1만 5900톤의 음식물 쓰레기가 배출되며, 음식 낭비로 연간 20조 이상의 경제 손실이 발생하고 있으며 전체 음식물 쓰레기 중 약 70%는 가정과 소형 음식점에서 발생하고 있다.

환경부에서 온실가스 중 메탄을 줄여 지구촌 기후 위기 대응에 동참하고자 글로벌 메탄 서약에 가입할 계획임을 밝혔다. 폐기물 부문에서는 유기성 폐기물(음식물 쓰레기 등) 발생량을 줄이는 것을 계획하고 있다.

우리가 만든 서비스를 이용하며 음식점이나 편의점에서는 버려야 할 음식들을 할인해서 판매할 수가 있고 그에 따른 음식물 쓰레기 처리 비용도 줄일 수 있을 것이고 일반 이용자의 경우 좀 더 할인된 가격으로 구매할 수
있다.

크게 보았을 때 소형 음식점에서의 음식물 쓰레기 발생량을 줄일 수 있을 것으로 예상된다.(공익성)

## 사이트맵

![siteMap](./TanDaMarketSiteMap.png)


## ✔설치

```  
$ git clone https://github.com/theNaeunTeam/theNaeunTeam-Final_Frontend.git  
$ cd theNaeunTeam-Final_Frontend  
$ yarn install  
$ yarn start  
```

## 개발환경

- IntelliJ
- WebStorm
- VSCode
- Android Studio
- Xcode 13.1
- GitHub
- Postman

## 사용기술

- Front-end
  - Node.js 16.13.1 LTS
  - TypeScript
  - React 17.0.2
  - React Native CLI 0.65.1
  - Redux
  - Material-UI
  - styled-components
  - SCSS
  - chart.js
  - FCM
  - Build Tools
    - NPM
    - yarn
    - gradle
    - CocoaPods


- Back-end
  - JAVA 17 LTS
  - Spring Boot 2.5.6
  - Spring Boot Security
  - Spring Boot Batch
  - JWT Web Token
  - MyBatis
  - Spring Boot Mail Server
  - FCM Push Server
  - Build Tools
    - Maven


- Database
  - Mysql


- AWS
  - EC2
  - S3
  - Route53
  - Load Balancer
  - RDS


- Google Firebase
  - Firebase Cloud Messaging

## 기능

**스프링 시큐리티 Role에 따라 4개의 사용자로 구분**

- 퍼블릭
- 유저
- 오너
- 마스터

### 공통

1. 인증성공시 JWT토큰을 로컬스토리지에 저장하고, 다음 번 접속 했을때 토큰의 유효성을 확인하고 유효하다면 자동으로 로그인 처리를 한다.
2. 권한이 필요한 페이지 접속시, API 요청시 백엔드와 프론트엔드 2중으로 인가 처리를 해서 URL 직접 접속 방지

### 유저

- 로그인, 회원가입 관련

  1. 유저, 가게 회원가입 로그인 가능
  2. 회원가입시 패스워드는 Hash암호화
  3. 인증과 인가는 JWT토큰방식을 사용
  4. 유저 비밀번호 분실시 비밀번호 재설정 메일 발송 기능
  5. 가게 입점 등록 시 지오코딩API를 이용해 해당 주소의 좌표 값 자동 등록
  6. 가게 입점등록 시 가게 대표 이미지는 아마존 S3로 관리
  7. 가게 입점등록 시 마스터의 승인이 있어야 로그인 가능


- 가게 찾기 관련 기능

  1. 현재 위치 기반으로 \*km내 가게 리스트 보기, 지도에 마커 표시 기능
  2. 가게 리스트에서 그 가게의 위치와 판매중인 상품 종류를 지도와 차트로 한눈에 볼 수 있다.
  3. \*km내 찾는 상품이 있는 가게 검색 기능
  4. 거리 순, 상품종류 많은 순으로 정렬 기능
  5. 현 위치 검색 성공 시 쿠키에 저장되어 GPS를 다시 검색하지 않고 바로 리스트 접근 가능, 메인 페이지에 근처 가게 표시

- 가게 상세페이지

  1. 한 페이지에서 상품정보, 매장정보 탭 전환이 가능하다.
  2. 상품 카테고리별 정렬 기능
  3. 즐겨찾는 가게 등록, 해제 가능.
  4. 장바구니 담기 기능. 단, 한 번에 한 가게의 상품만 담을 수 있다.
  5. 남은 재고 수량 이상으로 담을 수 없다.
  6. 매장 정보 페이지에서 가게의 전화번호, 영업시간 등 상세정보와 지도를 볼 수 있다.
  7. 매장 정보 패이지에서 주소 복사, 길찾기가 바로 가능하다.

- 장바구니, 주문하기 페이지
  1. 쿠키에 저장되어 재 접속 시에도 데이터가 유지된다.
  2. 장바구니 접속시마다 담긴 상품의 재고를 체크한다.
  3. 남은 재고 이상으로 담을 수 없다.
  4. 주문하기 페이지에서 방문하는사람, 방문시간, 방문일자, 요청사항 등을 입력할 수있다.
  5. 노쇼 카운트가 5 이상이면 주문이 불가하다.

- 유저 마이페이지
  1. 적립된 포인트, 주문횟수 확인가능
  2. 예약한 상품의 카테고리, 예약 상태, 상품명으로 검색/정렬 해서 확인이나 취소가 가능하다.
  3. 즐겨 찾는 가게 목록의 확인, 취소를 할 수 있다.
  4. 내 정보를 수정할 수 있다.
  5. 회원탈퇴가 가능하다.

### 오너(가게)

- 가게 마이페이지
  1. 일일 매출액, 월별 매출액, 연도별 매출액을 통계를 그래프로 정리해서 볼 수 있다.
  2. 시간대, 성별, 나이, 상품 카테고리 별 판매 현황 통계를 차트로 시각화 해서 볼 수 있다.
  3. 예약이 들어오면 FCM을 통해 브라우저 / 가게용 어플에 푸시 알림이 표시된다.
  4. (예약 처리 로직, 실패시 트랜젝셔널 처리로 인해 롤백됨)
  5. 노쇼 발생 건수&비율, 예약 취소 발생 건수&비율, 유통기한 경과 폐기 상품 개수&비율을 볼 수 있다.
  6. 웹에서 상품 등록이 가능하다. 상품 등록 시 업로드한 이미지는 AWS S3로 관리된다.
  7. 어플로 상품 등록이 가능하다. 상품 등록 시 사진을 바로 편집해서 올릴 수 있다.
  8. 내가 올린 상품의 조회가 가능하다. 상품 분류와 판매상태로 정렬할 수 있으며 상품명으로 검색해서 상품의 내용을 수정하거나 판매완료 처리가 가능하다.
  9. 내가 올린 상품의 예약 현황 조회가 가능하다. 상품 분류와 예약상태로 정렬이 가능하며 상품명으로 검색해서 예약상태를 변경할 수 있다.
  10. 어플로 예약 현황 조회가 가능하다. 예약이 들어온 상품에 대해 승인, 거절, 노쇼, 판매완료 처리를 할 수 있다.
  11. 이용해지신청을 할 수 있다.

- 배치 기능
  1. 매일 밤 00시에 현재날짜와 상품의 유통기한을 비교해 자동 상품 상태 변경

### 마스터

- 마스터 관리/통계 페이지
  1. 월별/연도별 유저 가입/탈퇴 현황을 그래프로 확인할 수 있다.
  2. 월별/연도별 가게 가입/탈퇴 현황을 그래프로 확인할 수 있다.
  3. 지역별 가게 분포를 확인할 수 있다.
  4. 입점 신청 대기 중인 가게들을 입점 승인/반려할 수 있다.
  5. 입점 해지 신청 후 대기 중인 가게들을 해지 승인/거절할 수 있다.
  6. 메인페이지의 배너를 추가/삭제 할 수 있다.