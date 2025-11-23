// 페이지 레이아웃 템플릿
// 실제 데이터 없이 구조만 정의하는 템플릿 컴포넌트
const BaseLayout = ({ children, className, ...props }) => {
  return (
    <div className={`base-layout ${className || ''}`} {...props}>
      {children}
    </div>
  );
};

export default BaseLayout;

