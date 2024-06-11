import { createContext, useContext, useState } from "react";

type ThemeContextType = {
  theme: "auto" | "light" | "dark";
  setTheme: React.Dispatch<React.SetStateAction<ThemeContextType["theme"]>>;
};

const ThemeContext = createContext<ThemeContextType>({
  theme: "light",
  setTheme: () => null,
});
ThemeContext.displayName = "IfnosThemeContext";

export const ThemeProvider: React.FC<React.PropsWithChildren> = ({
  children,
}) => {
  const [theme, setTheme] = useState<ThemeContextType["theme"]>("auto");

  return (
    <ThemeContext.Provider value={{ theme, setTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = (): ThemeContextType => {
  return useContext(ThemeContext);
};
