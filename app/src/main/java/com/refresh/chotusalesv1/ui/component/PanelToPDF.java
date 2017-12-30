//package com.refresh.chotusalesv1.ui.component;
//
///**
// * Created by Lenovo on 12/20/2017.
// */
//
//
//    import com.itextpdf.awt.PdfGraphics2D;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfTemplate;
//import com.itextpdf.text.pdf.PdfWriter;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics2D;
//import java.awt.event.WindowEvent;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import net.miginfocom.swing.MigLayout;
//
//    public class PanelToPDF {
//
//        private static JFrame frame= new JFrame();
//        private static JPanel view= new JPanel();
//        private static float pageWidth= PageSize.A4.getWidth();
//        private static float pageHeight= PageSize.A4.getHeight();
//
//        public static void main(String[] args) throws Exception {
//            System.out.println("Page width = " + pageWidth + ", height = " + pageHeight);
//
//            initPane();
//            createMultipagePDF();
//
//            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
//        }
//
//
//        private static void initPane() {
//            view.setLayout(new MigLayout());
//            view.setBackground(Color.WHITE);
//
//            for (int i= 1; i <= 160; ++i) {
//                JLabel label= new JLabel("This is a test! " + i);
//                label.setForeground(Color.BLACK);
//                view.add(label, "wrap");
//
//                JPanel subPanel= new JPanel();
//                subPanel.setBackground(Color.RED);
//                view.add(subPanel);
//            }
//
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(new Dimension(Math.round(pageWidth), Math.round(pageHeight)));
//            frame.add(view);
//            frame.setVisible(true);
//        }
//
//        private static void createMultipagePDF() throws Exception {
//            // Calculate the number of pages required. Use the preferred size to get
//            // the entire panel height, rather than the panel height within the JFrame
//            int numPages= (int) Math.ceil(view.getPreferredSize().height / pageHeight); // int divided by float
//
//            // Output to PDF
//            OutputStream os= new FileOutputStream("test.pdf");
//            Document doc= new Document();
//            PdfWriter writer= PdfWriter.getInstance(doc, os);
//            doc.open();
//            PdfContentByte cb= writer.getDirectContent();
//
//            // Iterate over pages here
//            for (int currentPage= 0; currentPage < numPages; ++currentPage) {
//                doc.newPage(); // not needed for page 1, needed for >1
//
//                PdfTemplate template= cb.createTemplate(pageWidth, pageHeight);
//                Graphics2D g2d= new PdfGraphics2D(template, pageWidth, pageHeight * (currentPage + 1));
//                view.printAll(g2d);
//                g2d.dispose();
//
//                cb.addTemplate(template, 0, 0);
//            }
//
//            doc.close();
//        }
//}
