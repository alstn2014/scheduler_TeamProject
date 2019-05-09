package scheduler.member.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomTextField extends JTextField {
	private Font originalFont;
	private Color originalForeground;

	private Color placeholderForeground = new Color(160, 160, 160);
	private Color placeholderForeground2 = new Color(0, 0, 0);
	private boolean textWrittenIn;

	public CustomTextField(int columns) {
		super(columns);
	}

	public void setFont(Font f) {
		super.setFont(f);
		if (isTextWrittenIn()) {
			originalFont = f;
		} else {
			originalFont = f;
		}
	}

	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (isTextWrittenIn()) {
			originalForeground = fg;
		} else {
			originalForeground = fg;
		}
	}

	public Color getPlaceholderForeground() {
		return placeholderForeground;
	}

	public Color getPlaceholderForeground2() {
		return placeholderForeground2;
	}

	public void setPlaceholderForeground(Color placeholderForeground) {
		this.placeholderForeground = placeholderForeground;
	}

	public void setPlaceholderForeground2(Color placeholderForeground2) {
		this.placeholderForeground2 = placeholderForeground2;
	}

	public boolean isTextWrittenIn() {
		return textWrittenIn;
	}

	public void setTextWrittenIn(boolean textWrittenIn) {
		this.textWrittenIn = textWrittenIn;
	}

	public void setPlaceholder(final String text) {

		this.customizeText(text);

		this.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				warn();
			}

			public void removeUpdate(DocumentEvent e) {
				warn();
			}

			public void changedUpdate(DocumentEvent e) {
				warn();
			}

			public void warn() {
				if (getText().trim().length() != 0) {
					setFont(originalFont);
					setForeground(originalForeground);
					setTextWrittenIn(true);
				} else {
					setFont(originalFont);
					setForeground(originalForeground);
					setTextWrittenIn(false);
				}

			}
		});

		this.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (!isTextWrittenIn()) {
					setText("");
					inputText(text);
				}
			}

			public void focusLost(FocusEvent e) {
				if (getText().trim().length() == 0) {
					customizeText(text);
				} else {
					inputText(text);
				}
			}

		});

	}

	private void customizeText(String text) {
		setText(text);
		setFont(new Font(getFont().getFamily(), Font.ITALIC, getFont().getSize()));
		setForeground(getPlaceholderForeground());
		setTextWrittenIn(false);
	}

	private void inputText(String text) {

		setFont(new Font(getFont().getFamily(), Font.BOLD, getFont().getSize()));
		setForeground(getPlaceholderForeground2());
		setTextWrittenIn(true);
	}

}
