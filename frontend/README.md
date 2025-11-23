# JangHakRun Frontend

장학런 프론트엔드 프로젝트입니다. React + Vite 기반으로 개발되며, 하이브리드 앱 개발을 목표로 합니다.

## 📋 목차

- [개발 환경 설정](#개발-환경-설정)
- [프로젝트 구조](#프로젝트-구조)
- [아토믹 디자인 패턴](#아토믹-디자인-패턴)
- [개발 가이드라인](#개발-가이드라인)
- [하이브리드 앱 전환 계획](#하이브리드-앱-전환-계획)
- [주요 스크립트](#주요-스크립트)

## 🚀 개발 환경 설정

### 필수 요구사항

- Node.js 18.x 이상
- npm 또는 yarn

### 설치 및 실행

```bash
# 의존성 설치
npm install

# 개발 서버 실행
npm run dev

# 프로덕션 빌드
npm run build

# 빌드 미리보기
npm run preview

# 린트 검사
npm run lint
```

## 📁 프로젝트 구조

```
frontend/src/
├── 📱 App.jsx                    # 메인 앱 컴포넌트
├── 📱 main.jsx                   # 앱 진입점
├── 📄 index.css                  # 글로벌 스타일
├── 📄 App.css                    # 앱 스타일
│
├── 📁 Page/                      # 페이지 컴포넌트들
│   └── index.js                  # 페이지 export 관리
│
├── 📁 Components/                # 재사용 가능한 컴포넌트 (아토믹 디자인)
│   ├── 📁 atoms/                 # 기본 컴포넌트 (Button, Input 등)
│   ├── 📁 molecules/             # 복합 컴포넌트 (Card, List 등)
│   ├── 📁 organisms/             # 복잡한 섹션 (Header, Footer 등)
│   ├── 📁 templates/             # 페이지 레이아웃 템플릿
│   └── index.js                  # 컴포넌트 export 관리
│
├── 📁 Store/                     # 상태 관리
│   └── index.js
│
├── 📁 Redux/                     # Redux 설정
│   ├── store.js                  # Redux store 설정
│   └── slices/                   # Redux slices
│       └── index.js
│
├── 📁 services/                  # API 서비스
│   ├── api.js                    # API 호출 함수
│   └── index.js
│
├── 📁 hooks/                     # 커스텀 훅
│   ├── useAuth.js                # 인증 관련 훅
│   └── index.js
│
├── 📁 utils/                     # 유틸리티 함수
│   ├── reactNativeBridge.js      # React Native 브리지
│   └── index.js
│
└── 📁 assets/                    # 정적 리소스
```

## 🎨 아토믹 디자인 패턴

이 프로젝트는 **아토믹 디자인(Atomic Design)** 패턴을 따릅니다. 컴포넌트를 5단계로 구분하여 재사용성과 유지보수성을 높입니다.

### 1. Atoms (원자)
가장 작은 단위의 재사용 가능한 컴포넌트입니다.

**예시:** `Button`, `Input`, `Icon`, `Label` 등

**사용 예:**
```jsx
import { Button, Input } from '@/Components';

<Button onClick={handleClick}>클릭</Button>
<Input type="text" placeholder="입력하세요" />
```

### 2. Molecules (분자)
Atoms를 조합하여 만든 단순한 컴포넌트입니다.

**예시:** `Card`, `List`, `Form`, `SearchBar` 등

**사용 예:**
```jsx
import { Card } from '@/Components';

<Card>
  <h3>제목</h3>
  <p>내용</p>
</Card>
```

### 3. Organisms (유기체)
Molecules와 Atoms를 조합하여 만든 복잡한 섹션입니다.

**예시:** `Header`, `Footer`, `Navigation`, `ProductList` 등

**사용 예:**
```jsx
import { Header } from '@/Components';

<Header>
  <Logo />
  <Navigation />
  <UserMenu />
</Header>
```

### 4. Templates (템플릿)
페이지의 레이아웃 구조를 정의합니다. 실제 데이터 없이 구조만 정의합니다.

**예시:** `BaseLayout`, `AuthLayout`, `DashboardLayout` 등

**사용 예:**
```jsx
import { BaseLayout } from '@/Components';

<BaseLayout>
  <Header />
  <main>{children}</main>
  <Footer />
</BaseLayout>
```

### 5. Pages (페이지)
Templates에 실제 데이터를 채워넣은 최종 페이지입니다.

**위치:** `src/Page/` 폴더

**예시:**
```jsx
// src/Page/HomePage.jsx
import { BaseLayout } from '@/Components';
import { Header, Footer } from '@/Components';

const HomePage = () => {
  return (
    <BaseLayout>
      <Header />
      <main>
        <h1>홈페이지</h1>
      </main>
      <Footer />
    </BaseLayout>
  );
};

export default HomePage;
```

## 📝 개발 가이드라인

### 컴포넌트 생성 규칙

1. **새 컴포넌트 추가 시:**
   - 적절한 아토믹 레벨에 맞는 폴더에 생성
   - 해당 폴더의 `index.js`에 export 추가
   - `Components/index.js`에서도 export 확인

2. **컴포넌트 네이밍:**
   - PascalCase 사용 (예: `UserCard`, `ProductList`)
   - 파일명과 컴포넌트명 일치

3. **페이지 컴포넌트:**
   - `src/Page/` 폴더에 생성
   - `Page/index.js`에 export 추가

### 상태 관리

- **로컬 상태:** `useState`, `useReducer` 사용
- **전역 상태:** Redux 또는 Store 폴더 활용
- **서버 상태:** `services/` 폴더의 API 함수 사용

### 스타일링

- 글로벌 스타일: `index.css`
- 컴포넌트별 스타일: CSS Modules 또는 인라인 스타일
- 공통 스타일 변수는 `index.css`에 정의

### API 호출

- 모든 API 호출은 `services/api.js`에 정의
- 커스텀 훅을 통해 API 호출 로직 캡슐화

```jsx
// services/api.js
export const fetchUserData = async (userId) => {
  // API 호출 로직
};

// hooks/useUser.js
import { fetchUserData } from '@/services';

export const useUser = (userId) => {
  // 훅 로직
};
```

## 🔄 하이브리드 앱 전환 계획

이 프로젝트는 **웹에서 먼저 구상하고 개발한 후, 하이브리드 앱으로 전환**하는 전략을 따릅니다.

### 개발 단계

1. **웹 개발 단계 (현재)**
   - React + Vite로 웹 앱 개발
   - 모든 기능을 웹에서 먼저 구현
   - 반응형 디자인 적용

2. **하이브리드 앱 전환 단계**
   - React Native 또는 Capacitor 사용
   - `utils/reactNativeBridge.js`를 활용한 네이티브 기능 연동
   - 웹 코드 재사용 최대화

### React Native Bridge

`utils/reactNativeBridge.js`는 웹과 네이티브 앱 간 통신을 위한 브리지입니다.

**사용 예:**
```jsx
import { callNativeFunction } from '@/utils/reactNativeBridge';

// 네이티브 기능 호출
callNativeFunction('openCamera', { options });
```

### 전환 시 주의사항

- 컴포넌트를 플랫폼 독립적으로 작성
- 네이티브 기능은 브리지를 통해 추상화
- 스타일링은 플랫폼별로 분기 처리 가능하도록 설계

## 🛠 주요 스크립트

```bash
# 개발 서버 실행 (http://localhost:5173)
npm run dev

# 프로덕션 빌드
npm run build

# 빌드 결과 미리보기
npm run preview

# 코드 린트 검사
npm run lint
```

## 📚 추가 리소스

- [React 공식 문서](https://react.dev/)
- [Vite 공식 문서](https://vite.dev/)
- [아토믹 디자인 패턴](https://atomicdesign.bradfrost.com/)

## 🤝 기여 가이드

1. 새로운 기능 개발 시 적절한 아토믹 레벨에 컴포넌트 생성
2. 페이지는 `Page/` 폴더에 추가
3. API 호출은 `services/` 폴더에 정의
4. 커스텀 훅은 `hooks/` 폴더에 추가
5. 코드 작성 후 린트 검사 실행

---

**개발 시작하기:** `npm install && npm run dev`
