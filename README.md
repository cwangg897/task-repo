


### 멀티모듈(msa) 구조
- common :  공통모듈로서 예외와, 유틸성클래스들을 모아두었습니다
- profile-search : 프로필 조회와 유저관련 모듈입니다
- payment : 결제 관련 모듈입니다
- payment-external : 결제 외부api 모듈입니다.

JPA를 사용하다보니 연관관계를 맺으면서 외래키를 가지고 있는게 
바운디드컨텍스트로 따졌을 경우 payment는 다른 도메인이라고 생각했습니다.




## 프로필 상세조회 - 방법 3가지 
구현 방법은 여러가지가 있다고 생각합니다.

### 1. profile에 조회수 column을 두는방법 - redis로 map에 넣어두고 값을 증가시키는 방법입니다
Redis의 싱글스레드를 이용하여 조회를 할때마다 redis에 Hash자료구조를 사용하여 key는 profileId를 두고 증가 시키고 
스케줄링을 통해서 조회수를 증가시키는 방법이 있습니다. 
redis의 cache를 통해서 매번 업데이트를 치는것을 보여주기보다 스케줄러가 끝낼때 캐시데이터를 적재시켜서
업데이트를 보여주는 방법입니다.
고가용성을 생각하면 redis가 전부 마비된 경우에는 자바의 ConcurrentHashMap을 통해 저장을하고
이를 스케줄링을 통해 업데이트하는 것이 생각났습니다.


### 2. profile에 조회수 column을 두는방법 - 비동기로 업데이트하기
profile조회할 때 마다 조회수를 증가시키는데 증가시키는 로직을 비동기 처리로 따로 처리합니다.
동시성 문제가 발생하기때문에 Lock을 통해 증가시킬거 같습니다. 


### 3. 프로필 조회로그 테이블 만들어서 처리하기 (제가 선택한 방법)
1,2 번 방법을 택하지 않은 이유는 저는 처음부터 프로필을 조회하면 프로필을 조회한 사람이
누구인지 로그를 저장하려고 해서 추후 데이터로 활용한다면 좋을 것이라고 생각했기떄문에 프로필 조회 테이블을 만들었습니다. 
프로필을 조회할때 이벤트를 발생시켜 로그를 저장하도록 설계했습니다. 
Spring의 자체적인 이벤트시스템인 ApplicationEventPublisher 사용했습니다. 
위에서 `프로필을 누가조회했는지` 가 데이터로 활용될 수도 있지만 
프로필 조회로그 데이터는 과연 꼭 안전하게 저장되어할 정도로 중요한가? 라는 의문을 가졌습니다. <br>
이에 대한 답변으로는 [Pull Request #13](https://github.com/cwangg897/task-repo/pull/13) 에 적었습니다


## 프로필 목록 조회
프로필 목록조회는 페이지네이션을 활용했습니다.
큰 어려움없이 페이지네이션을 만들었는데 common모듈에 페이지네이션 포맷을 만들어서 사용했습니다.
왜냐하면 spring data jpa에서도 제공해주지만 spring data jpa를 사용하지 않는 경우도 존재하기때문입니다.

## 트래픽 대비하여 캐시 - Redis 고가용성 대비
프로필 상세조회와, 프로필 목록조회에는 캐시를 사용해서 트래픽에 대비했습니다.
Redis를 사용하다가 만약에 Redis가 다운된다면? 
이를 대비하면 서킷브레이커 패턴을 사용해서 Redis다운되면 직접 DB에 select하게 만들었습니다.
물론 이는 굉장히 위험할 수 도있습니다. 왜냐하면 db에 많은 트래픽이 가다가 db가 다운되면 전체적인 시스템에 영향을 주기때문입니다.
그러나 redis의 고가용성 전략으로 센티널이나 혹은 클러스터를 사용하다가 복구작업을 한다면 오래걸리지 않을 뿐더러
프로덕션 환경에서는 주로 DB도 master-slave를 따르기떄문에 읽기전용 db만 사용한다면 redis복구시간 동안은 안전할 거라고 생각했습니다.

## 결제 로직

### 1. 결제 요청 
프론트에서 승인요청값과 맞는지 확인하기위해 결제 위젯에서 결제전에 요청을 보내서 저장합니다



### 요청 성공한다면 -> 결제 승인
전략패턴, 이벤트, 상태변경 아마도 요청실패한것은 추후 배치작업을 통해 지우거나 상태변경 작업을 하면 좋다고생각합니다.


### 요청 실패한다면 - 실패 처리


### 포인트 지급
포인트 지급도 만약 결제승인은 성공했는데 포인트 지급 실패가 나거나 누락된 경우가 만약 존재한다면
이를 대비하면 배치성으로 포인트 지급 성공했는데 포인트 지급결과(로그)가 없다면 이를 지급하도록 만들거 같습니다.


## 토스 PG외부 api



