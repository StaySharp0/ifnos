import { styled } from "@stitches/react";
import type { VariantProps } from "@stitches/react";

export interface ButtonProps {
  /**
   * Is this the principal call to action on the page?
   */
  mode?: "primary" | "secondary";
  /**
   * How large should the button be?
   */
  size?: "small" | "medium" | "large";
  /**
   * Button contents
   */
  label: string;
  /**
   * Optional click handler
   */
  onClick?: () => void;
}

/**
 * Primary UI component for user interaction
 */
export const Button = ({
  mode = "primary",
  size = "medium",
  label,
  ...props
}: ButtonProps) => {
  return (
    <ButtonStyled
      mode={mode as ButtonStyledVariantsType["mode"]}
      size={size as ButtonStyledVariantsType["size"]}
      {...props}
    >
      {label}
    </ButtonStyled>
  );
};

type ButtonStyledVariantsType = VariantProps<typeof ButtonStyled>;

const ButtonStyled = styled("button", {
  fontWeight: 700,
  border: 0,
  borderRadius: "3em",
  cursor: "pointer",
  display: "inline-block",
  lineHeight: 1,

  variants: {
    mode: {
      primary: {
        color: "white",
        backgroundColor: "#1ea7fd",
      },
      secondary: {
        color: "#333",
        backgroundColor: "transparent",
        boxShadow: "rgba(0, 0, 0, 0.15) 0px 0px 0px 1px inset",
      },
    },
    size: {
      small: {
        fontSize: "12px",
        padding: "10px 16px",
      },
      medium: {
        fontSize: " 14px",
        padding: "11px 20px",
      },
      large: {
        fontSize: " 16px",
        padding: "12px 24px",
      },
    },
  },
});
