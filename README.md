# KCS-S1-Dev-UR1PICK
카카오 클라우드 스쿨 1기 개발자과정 UR1PICK팀 최종프로젝트 백엔드

본 Repository는 별도로 운영하던 GitLab에서 복제했기 때문에 커밋 로그 등이 남아있지 않음

현재 AWS 비용 문제로 본 서비스는 비활성화됨

참고자료 : https://bit.ly/3jv5DJi

---

작업기간 : 2022. 11. 01 ~ 2022. 12. 12

인력구성 : 총 4명 / BE 1명, FE 1명, ELK Stack 1명, CI/CD 구축 1명

프로젝트 목적 : 악성 사이트의 접속경로로 악용되는 경우가 많은 단축 URL 서비스 개선

수행역할 : 

1. 백엔드 API 개발
- Java, SpringBoot를 이용해 단축 URL 서비스에 필요한 백엔드 API 구현
- ElasticSearch를 이용한 접속자 통계 추출
- Google OAuth2를 연동하여 인증/회원가입 구현
- 악성 사이트 필터링 기능 개발
- 서비스 모듈 분할로 유지보수성 증가 및 결합도 감소 
- 취약점 사이트 프리뷰 생성 구현
◦ 이 작업은 시간이 걸리기 때문에 비동기적으로 구현
- 주간 통계 리포트 생성 기능 구현
- Google SMTP와 연동하여 메일 전송 서비스 구현
2. Kubernetes 기반의 서비스 구축
- 서비스 구성 모듈 컨테이너화 및 경량화 수행
- HPA를 이용한 Auto Scailing 구현
3. 컨테이너 기반 데이터베이스 클러스터 구성
- Kubernetes 인프라에서 작동하는 MySQL 클러스터 구성
- Stateful Pod와 PV, PVC를 이용한 데이터베이스 영속성 유지
4. AWS 환경으로 RePlatform, 이에 따른 아키텍처 변경
- 기존 온프레미스에 구축된 Kubernetes 기반 서비스를 AWS EKS로 마이그레이션
- 아키텍처를 구성하는 일부 기능을 AWS 서비스로 대체
- 서비스 부하에 따라 자동으로 인프라 확장 설정
- AWS CloudFormation을 이용한 IaC 구축
- 보안그룹, NACL 규칙 검토 및 IAM 관리를 통한 최소 권한 원칙 준수 노력
4. 백엔드 소스코드 보안성 검토
- 행정안전부 개발보안 가이드 기반 취약점 검토
- 의존성 라이브러리에 대한 취약점 점검

![개발자반_1조_최종발표-11](https://github.com/CptBluebear/KCS-S1-Dev-UR1PICK/assets/28571717/1f267386-66b9-4973-9d68-70133b3a386c)
![개발자반_1조_최종발표-12](https://github.com/CptBluebear/KCS-S1-Dev-UR1PICK/assets/28571717/8a5eb504-6632-4b1f-8baf-e2e4f7c2e46a)
![개발자반_1조_최종발표-18](https://github.com/CptBluebear/KCS-S1-Dev-UR1PICK/assets/28571717/504ee6fa-57b1-477d-8001-0b1bd9c1e68e)
![개발자반_1조_최종발표-19](https://github.com/CptBluebear/KCS-S1-Dev-UR1PICK/assets/28571717/c909fc44-b38c-454b-b294-5641cdb9cf80)
![개발자반_1조_최종발표-34](https://github.com/CptBluebear/KCS-S1-Dev-UR1PICK/assets/28571717/9d6590e0-32be-4cea-a977-17845d83bd78)
![개발자반_1조_최종발표-44](https://github.com/CptBluebear/KCS-S1-Dev-UR1PICK/assets/28571717/95c42526-8b45-42ff-900b-2509d83d665e)
