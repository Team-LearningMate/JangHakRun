const Footer = ({ children, className, ...props }) => {
  return (
    <footer className={`footer ${className || ''}`} {...props}>
      {children}
    </footer>
  );
};

export default Footer;

