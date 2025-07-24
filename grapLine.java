import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class grapLine {
    private Color lineColor;
    private Color pointColor;
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private List<Point2D.Double> graphPoints = new ArrayList<>();
    private List<Double> scores;
    private int pointWidth = 4;

    public grapLine(List<Double> scores, Color lineColor, Color pointColor) {
        this.scores = scores;
        this.lineColor = (lineColor != null) ? lineColor : generateRandomColor();
        this.pointColor = (pointColor != null) ? pointColor : this.lineColor.darker();
    }

    // คำนวณจุดด้วย min/max จากภายนอก
    public void calculatePoints(int width, int height, int padding, int labelPadding, 
                              double globalMin, double globalMax) {
        graphPoints.clear();
        if (scores.isEmpty()) return;

        double range = (globalMax == globalMin) ? 1 : (globalMax - globalMin);
        double xScale = ((double) width - (2 * padding) - labelPadding) / (scores.size() - 1);
        double yScale = ((double) height - 2 * padding - labelPadding) / range;
        
        for (int i = 0; i < scores.size(); i++) {
            double x = padding + labelPadding + (i * xScale);
            double y = (height - padding - labelPadding) - ((scores.get(i) - globalMin) * yScale);
            graphPoints.add(new Point2D.Double(x, y));
        }
    }

    public void draw(Graphics2D g2) {
        if (graphPoints.isEmpty()) return;

        // วาดเส้นกราฟ
        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            Point2D.Double p1 = graphPoints.get(i);
            Point2D.Double p2 = graphPoints.get(i + 1);
            g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
        }
        g2.setStroke(oldStroke);

        // วาดจุดข้อมูล
        g2.setColor(pointColor);
        for (Point2D.Double point : graphPoints) {
            double x = point.x - pointWidth / 2.0;
            double y = point.y - pointWidth / 2.0;
            g2.fill(new Ellipse2D.Double(x, y, pointWidth, pointWidth));
        }
    }

    public double getMinScore() {
        if (scores.isEmpty()) return 0;
        double minScore = scores.get(0);
        for (Double score : scores) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    public double getMaxScore() {
        if (scores.isEmpty()) return 0;
        double maxScore = scores.get(0);
        for (Double score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public List<Double> getScores() {
        return scores;
    }

    public void setLineColor(Color color) {
        this.lineColor = color;
    }

    public void setPointColor(Color color) {
        this.pointColor = color;
    }
    private Color generateRandomColor() {
        Random rand = new Random();
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 180);
    }
}