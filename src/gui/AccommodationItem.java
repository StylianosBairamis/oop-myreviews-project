package gui;

import api.Accommodation;
import javax.swing.JButton;
import javax.swing.JLabel;

public class AccommodationItem extends JButton
{
    private Accommodation item;
    private JLabel nameText;
    private JLabel reviewText;
    AccommodationItem(Accommodation item, String nameText, String reviewText) {
        this.item = item;
        this.nameText = new JLabel(nameText);
        this.add(this.nameText);
        this.reviewText = new JLabel(reviewText);
        this.add(this.reviewText);
    }

    public Accommodation getAccommodationInfo() {
        return this.item;
    }
    public void setReviewLabel(String reviewText) {
        this.reviewText.setText(reviewText);
    }
}
