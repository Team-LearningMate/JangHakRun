const List = ({ children, className, ...props }) => {
  return (
    <ul className={`list ${className || ''}`} {...props}>
      {children}
    </ul>
  );
};

export default List;

