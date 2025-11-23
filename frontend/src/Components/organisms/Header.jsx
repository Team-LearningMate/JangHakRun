const Header = ({ children, className, ...props }) => {
  return (
    <header className={`header ${className || ''}`} {...props}>
      {children}
    </header>
  );
};

export default Header;

