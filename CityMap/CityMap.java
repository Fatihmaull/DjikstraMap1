import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityMap extends JFrame {
    private Graph cityGraph;
    private JTextField startField, endField;
    private JTextArea resultArea;
    private MapPanel mapPanel;

    public CityMap() {
        setTitle("Maps Sederhana dengan Dijkstra");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cityGraph = new Graph();
        initializeGraph();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Lokasi Awal:"));
        startField = new JTextField();
        inputPanel.add(startField);

        inputPanel.add(new JLabel("Lokasi Tujuan:"));
        endField = new JTextField();
        inputPanel.add(endField);

        JButton calculateButton = new JButton("Hitung Rute Terpendek");
        calculateButton.addActionListener(new CalculateButtonListener());
        inputPanel.add(calculateButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        mapPanel = new MapPanel();

        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(resultArea), BorderLayout.SOUTH);
        getContentPane().add(mapPanel, BorderLayout.CENTER);
    }

    private void initializeGraph() {
        cityGraph.addEdge("CIBIRU", "MANISI", 4);
        cityGraph.addEdge("CIBIRU", "CIPADUNG", 2);
        cityGraph.addEdge("MANISI", "CIPADUNG", 5);
        cityGraph.addEdge("MANISI", "SOETTA", 10);
        cityGraph.addEdge("CIPADUNG", "UBER", 3);
        cityGraph.addEdge("UBER", "SOETTA", 4);
        cityGraph.addEdge("SOETTA", "KIARACONDONG", 11);
        cityGraph.addEdge("UBER", "KIARACONDONG", 2);
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String start = startField.getText().trim();
            String end = endField.getText().trim();

            if (start.isEmpty() || end.isEmpty()) {
                JOptionPane.showMessageDialog(CityMap.this, "Harap masukkan lokasi awal dan tujuan.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, Integer> distances = cityGraph.dijkstra(start);
            if (!distances.containsKey(end)) {
                resultArea.setText("Tidak ada rute dari " + start + " ke " + end + ".");
            } else {
                resultArea.setText("Jarak terpendek dari " + start + " ke " + end + " adalah " + distances.get(end) + ".");
            }
        }
    }

    private class MapPanel extends JPanel {
        private final int RADIUS = 20;
        private final Map<String, Point> locations;
        private final Map<String, Map<String, Integer>> edges;

        public MapPanel() {
            this.locations = Map.of(
                "CIBIRU", new Point(100, 100),
                "MANISI", new Point(200, 100),
                "CIPADUNG", new Point(150, 200),
                "SOETTA", new Point(300, 300),
                "UBER", new Point(200, 300),
                "KIARACONDONG", new Point(400, 400)
            );
            this.edges = Map.of(
                "CIBIRU", Map.of("MANISI", 4, "CIPADUNG", 2),
                "MANISI", Map.of("CIPADUNG", 5, "SOETTA", 10),
                "CIPADUNG", Map.of("UBER", 3),
                "UBER", Map.of("SOETTA", 4, "KIARACONDONG", 2),
                "SOETTA", Map.of("KIARACONDONG", 11)
            );
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);

            // Menggambar edges dan nilai edges
            for (Map.Entry<String, Map<String, Integer>> entry : edges.entrySet()) {
                String from = entry.getKey();
                for (Map.Entry<String, Integer> edge : entry.getValue().entrySet()) {
                    String to = edge.getKey();
                    int weight = edge.getValue();
                    drawEdge(g, from, to, weight);
                }
            }

            // Menggambar nodes
            for (Map.Entry<String, Point> entry : locations.entrySet()) {
                int x = entry.getValue().x;
                int y = entry.getValue().y;
                g.setColor(Color.RED);
                g.fillOval(x - RADIUS / 2, y - RADIUS / 2, RADIUS, RADIUS);
                g.setColor(Color.BLACK);
                g.drawString(entry.getKey(), x - RADIUS / 2, y - RADIUS / 2);
            }
        }

        private void drawEdge(Graphics g, String from, String to, int weight) {
            Point p1 = locations.get(from);
            Point p2 = locations.get(to);
            if (p1 != null && p2 != null) {
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;
                g.drawString(String.valueOf(weight), midX, midY);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CityMap frame = new CityMap();
            frame.setVisible(true);
        });
    }
}
