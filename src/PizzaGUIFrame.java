import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.round;

public class PizzaGUIFrame extends JFrame
{
    private JPanel mainPanel;
    private JPanel pizzaPanel;
    private JPanel orderPanel;
    private JPanel optionsPanel;
    private JPanel crustOptions;
    private ArrayList<JRadioButton> crustButtons = new ArrayList<>();
    private JPanel sizeOptions;
    private JComboBox sizeCombo;
    private JPanel toppingsOptions;
    private JTextArea orderTextArea;
    private JScrollPane orderScrollPane;
    private JButton orderButton;
    private JButton clearButton;
    private JButton quitButton;
    private JOptionPane optionPane;

    private String[] sizes = {"Small", "Medium", "Large", "Super"};
    private double[] sizesPrices = {8.00, 12.00, 16.00, 20.00};
    private String selectedSize = "DEFAULT";
    private double selectedSizePrice = 0;
    private String[] toppings = {"Pepperoni", "Parmesan", "Sausage", "Pineappple", "Ham", "Peppers"};
    private ArrayList<String> selectedToppings = new ArrayList<>();
    private double selectedToppingPrice = 0;
    private String[] crusts = {"Thin", "Regular", "Deep-dish"};
    private double[] crustsPrices = {8.00, 10.00, 12.00};
    private String selectedCrust = "DEFAULT";
    private double selectedCrustPrice = 0;
    private boolean crustSelected = false;
    private boolean sizeSelected = false;
    public PizzaGUIFrame()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3,1));
        CreatePizzaPanel();
        CreateOrderPanel();
        CreateOptionsPanel();
        optionPane = new JOptionPane();
        add(mainPanel);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize((screenWidth / 4) * 3, screenHeight);
        setLocation(screenWidth / 8, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void CreatePizzaPanel()
    {
        pizzaPanel = new JPanel();
        pizzaPanel.setLayout(new GridLayout(1,2));
        JPanel secondaryPanel = new JPanel();
        secondaryPanel.setLayout(new GridLayout(2,1));
        crustOptions = new JPanel();
        crustOptions.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Crusts", TitledBorder.LEFT, TitledBorder.TOP));
        for (int i = 0; i < crusts.length; i++)
        {
            JRadioButton button = new JRadioButton(crusts[i]);
            crustButtons.add(button);
            button.addActionListener(e ->
            {
                for (JRadioButton b: crustButtons)
                {
                    b.setSelected(false);
                }
                if (!button.isSelected())
                {

                    button.setSelected(true);
                    selectedCrust = button.getText();
                    selectedCrustPrice = crustsPrices[Arrays.asList(crusts).indexOf(button.getText())];
                    crustSelected = true;

                }
                else
                {
                    selectedCrust = "";
                    selectedCrustPrice = 0;
                    crustSelected = false;
                }
            });
            crustOptions.add(button);
        }
        secondaryPanel.add(crustOptions);

        sizeOptions = new JPanel();
        sizeOptions.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Sizes", TitledBorder.LEFT, TitledBorder.TOP));
        sizeCombo = new JComboBox(sizes);
        sizeCombo.addActionListener(e ->
        {
                sizeSelected = true;
                selectedSize = sizeCombo.getSelectedItem().toString();
                selectedSizePrice = sizesPrices[sizeCombo.getSelectedIndex()];
        });
        sizeOptions.add(sizeCombo);
        secondaryPanel.add(sizeOptions);

        toppingsOptions= new JPanel();
        toppingsOptions.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Toppings", TitledBorder.LEFT, TitledBorder.TOP));
        for (String topping: toppings)
        {
            JCheckBox checkBox = new JCheckBox(topping);
            toppingsOptions.add(checkBox);
            checkBox.addActionListener(e ->
            {
                if (checkBox.isSelected() == true)
                {
                    selectedToppings.add(topping);
                }
                else
                {
                    selectedToppings.remove(topping);
                }
            });
        }
        pizzaPanel.add(secondaryPanel);
        pizzaPanel.add(toppingsOptions);
        mainPanel.add(pizzaPanel);
    }
    public void CreateOrderPanel()
    {
        orderPanel = new JPanel();
        orderPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Order Details", TitledBorder.LEFT, TitledBorder.TOP));
        orderPanel.setLayout(new GridLayout(1,1));
        orderTextArea = new JTextArea(10, 30);
        orderScrollPane = new JScrollPane(orderTextArea);
        orderPanel.add(orderScrollPane);
        mainPanel.add(orderPanel);
    }
    public void CreateOptionsPanel()
    {
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(1,3));
        orderButton = new JButton("ORDER");
        orderButton.addActionListener(e ->
        {
            if (crustSelected == true)
            {
                if (sizeSelected == true)
                {
                    CreateOrder();
                }
                else
                {
                    optionPane.showInternalMessageDialog(null, "Please choose a size",
                            "Missing Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else
            {
                optionPane.showInternalMessageDialog(null, "Please choose a crust",
                        "Missing Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        clearButton = new JButton("CLEAR");
        clearButton.addActionListener(e ->
        {
            ClearInfo();
        });
        quitButton = new JButton("QUIT");
        quitButton.addActionListener(e ->
        {
            QuitApp();
        });
        optionsPanel.add(orderButton);
        optionsPanel.add(clearButton);
        optionsPanel.add(quitButton);
        mainPanel.add(optionsPanel);
    }
    public void CreateOrder()
    {
        orderTextArea.setText("");
        orderTextArea.append("=========================================\n");
        orderTextArea.append(selectedCrust + "     $" + selectedCrustPrice+ "\n");
        orderTextArea.append(selectedSize + "         $" + selectedSizePrice + "\n");
        for (String selectedTopping: selectedToppings)
        {
            orderTextArea.append(selectedTopping + "     $1.00\n");
        }
        double subTotal = CalculateSubTotal();
        orderTextArea.append("\nSub-total:     $" + subTotal);
        orderTextArea.append("\nTax:           $" + String.format("%.2f", (subTotal * 0.07)));
        orderTextArea.append("\n---------------------------------------------------------------------");
        orderTextArea.append("\nTotal:         $" + String.format("%.2f",(subTotal * 1.07)));
        orderTextArea.append("\n=========================================");
    }
    private double CalculateSubTotal()
    {
        double subTotal = 0;
        subTotal = selectedCrustPrice + selectedSizePrice + selectedToppings.size();
        return subTotal;
    }
    private void QuitApp()
    {
        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }
    private void ClearInfo()
    {
        this.dispose();
        new PizzaGUIFrame();
    }
}
