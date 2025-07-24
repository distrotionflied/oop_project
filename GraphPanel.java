import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.List;

public class GraphPanel extends JPanel {
    private int padding = 25;
    private int labelPadding = 25;
    private Color gridColor = new Color(200, 200, 200, 200);
    private int numberYDivisions = 10;
    private List<grapLine> graphLines = new ArrayList<>();

    // คอนสตรักเตอร์รับหลายเส้น
    public GraphPanel(List<grapLine> graphLines) {
        this.graphLines = graphLines;
    }

    // คอนสตรักเตอร์รับเส้นเดียว
    public GraphPanel(grapLine graphLine) {
        this.graphLines.add(graphLine);
    }

    // เพิ่มเมธอดเพิ่มเส้น
    public void addLine(grapLine line) {
        this.graphLines.add(line);
        repaint();
    }

    // ลบเส้นทั้งหมด
    public void clearLines() {
        this.graphLines.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // หาค่า min/max รวมจากทุกเส้น
        double minScore = getGlobalMinScore();
        double maxScore = getGlobalMaxScore();

        // วาดพื้นหลัง
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, 
                   getWidth() - (2 * padding) - labelPadding, 
                   getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // วาดกริดและแกน Y (ใช้ค่า min/max รวม)
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = x0 + 5;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            
            g2.setColor(gridColor);
            g2.drawLine(padding + labelPadding + 5, y0, getWidth() - padding, y0);
            g2.setColor(Color.BLACK);
            
            double value = minScore + (maxScore - minScore) * (i * 1.0 / numberYDivisions);
            String yLabel = String.format("%.2f", value);
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabel);
            g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            g2.drawLine(x0, y0, x1, y0);
        }

        // วาดเส้นกราฟทุกเส้น
        for (grapLine line : graphLines) {
            line.calculatePoints(getWidth(), getHeight(), padding, labelPadding, minScore, maxScore);
            line.draw(g2);
        }

        // วาดแกน X และ Y
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, 
                   padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, 
                   getWidth() - padding, getHeight() - padding - labelPadding);
    }

    private double getGlobalMinScore() {
        return graphLines.stream()
                .mapToDouble(grapLine::getMinScore)
                .min()
                .orElse(0);
    }

    private double getGlobalMaxScore() {
        return graphLines.stream()
                .mapToDouble(grapLine::getMaxScore)
                .max()
                .orElse(10); // ค่า default ถ้าไม่มีข้อมูล
    }

    private static void createAndShowGui() {
        // สร้างข้อมูลตัวอย่าง
        List<Double> scores = new ArrayList<>();
        Random random = new Random();
        int maxDataPoints = 10;
        int maxScore = 10;
        for (int i = 0; i < maxDataPoints; i++) {
            scores.add(random.nextDouble() * maxScore);
        }

        // สร้างอ็อบเจ็กต์ grapLine
        grapLine line = new grapLine(
            scores, 
            new Color(44, 102, 230, 180), // สีเส้น
            new Color(100, 100, 100, 180)  // สีจุด
        );

        // ส่งอ็อบเจ็กต์ให้ GraphPanel
        GraphPanel mainPanel = new GraphPanel(line);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //สำหรับ เทส
        SwingUtilities.invokeLater(() -> {
            createAndShowGui();
        });
    }
}