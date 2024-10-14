package umg.programacion2.Reportes;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import umg.programacion2.DataBase.Model.ProductoModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import static com.itextpdf.text.BaseColor.LIGHT_GRAY;

public class PdfReport {
    private static final Font TITLE_FONT = FontFactory.getFont("Georgia", 14, Font.BOLD);
    private static final Font HEADER_FONT = FontFactory.getFont("Garamond", 12, Font.BOLD);
    private static final Font NORMAL_FONT = FontFactory.getFont("Palatino", 12, Font.NORMAL);

    public void generateProductReport(List<ProductoModel> productos, String outputPath, boolean agrupar) throws DocumentException, IOException {
        Document document = new Document(PageSize.LETTER, 55, 55, 55, 55);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();
        addTitle(document);

        // Llamar método según sea para el valor de agrupar
        if (agrupar) {
            addProductTableGrouped(document, productos);
        } else {
            addProductTable(document, productos);
        }
        document.close();
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("REPORTE DE PRODUCTOS\n" +
                "Estudiante: Karla Raquel Perez Espino, Carnet: 0905-21-10852",  TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);
    }

    //Tabla sin agrupación
    private void addProductTable(Document document, List<ProductoModel> productos) throws DocumentException {
        PdfPTable table = new PdfPTable(6); //Columnas: ID, Descripción, Origen, Precio, Existencia, Precio Total
        table.setWidthPercentage(100);
        addTableHeader(table);
        addRows(table, productos);
        document.add(table);
    }

    //Tabla con agrupación por origen
    private void addProductTableGrouped(Document document, List<ProductoModel> productos) throws DocumentException {
        PdfPTable table = new PdfPTable(6); //Columnas: ID, Descripción, Origen, Precio, Existencia, Precio Total
        table.setWidthPercentage(100);
        addTableHeader(table);
        addRowsGroup(table, productos);
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        String[] columnTitles = {"ID", "Descripción", "Origen", "Precio", "Existencia", "Precio Total"};
        for (String columnTitle : columnTitles) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.RED);
            header.setBorderWidth(1);
            header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
            table.addCell(header);
        }
    }

    //Método sin agrupación (original)
    private void addRows(PdfPTable table, List<ProductoModel> productos) {
        for (ProductoModel producto : productos) {
            table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
            table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));
            table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));
            DecimalFormat df = new DecimalFormat("#.00");
            String cantidadFormateada = df.format(producto.getExistencia() * producto.getPrecio());
            table.addCell(new Phrase(cantidadFormateada, NORMAL_FONT));
        }
    }

    //Método con agrupación por origen
    private void addRowsGroup(PdfPTable table, List<ProductoModel> productos) {
        String currentOrigen = null;
        double groupTotalPrecio = 0.0;
        int groupTotalExistencia = 0;
        double Precioindividual = 0.0;

        DecimalFormat df = new DecimalFormat("#.00");
        BaseColor writeColor = LIGHT_GRAY;
        Font boldFont = new Font(NORMAL_FONT.getFamily(), NORMAL_FONT.getSize(), Font.BOLD);

        for (ProductoModel producto : productos) {
            if (currentOrigen == null) {

                //Primer pais
                currentOrigen = producto.getOrigen();
                //Agregar fila del pais
                PdfPCell groupCell = new PdfPCell(new Phrase("País: " + currentOrigen, NORMAL_FONT));
                groupCell.setColspan(6);
                table.addCell(groupCell);

            } else if (!producto.getOrigen().equals(currentOrigen)) {

                PdfPCell totalCellLabel = new PdfPCell(new Phrase("Total de Productos del país: " + currentOrigen, boldFont));
                totalCellLabel.setColspan(4);
                totalCellLabel.setBackgroundColor(LIGHT_GRAY);
                table.addCell(totalCellLabel);

                PdfPCell totalExistenciaCell = new PdfPCell(new Phrase(String.valueOf(groupTotalExistencia), boldFont));
                totalExistenciaCell.setBackgroundColor(LIGHT_GRAY);
                table.addCell(totalExistenciaCell);

                PdfPCell totalPrecioCell = new PdfPCell(new Phrase(df.format(groupTotalPrecio), boldFont));
                totalPrecioCell.setBackgroundColor(LIGHT_GRAY);
                table.addCell(totalPrecioCell);

                //Reiniciar totales del nuevo pais
                groupTotalPrecio = 0.0;
                groupTotalExistencia = 0;

                //Actualizar el origen actual del nuevo pais
                currentOrigen = producto.getOrigen();

                //Agregar fila de nuevo pais
                PdfPCell groupCell = new PdfPCell(new Phrase("País: " + currentOrigen, NORMAL_FONT));
                groupCell.setColspan(6);
                table.addCell(groupCell);
            }

            //Agregar fila del producto
            table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
            table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));
            table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));

            //Añadir el precio individual dos decimales
            Precioindividual = producto.getExistencia() * producto.getPrecio();
            table.addCell(new Phrase(df.format(producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));
            String cantidadFormateada = df.format(Precioindividual);
            table.addCell(new Phrase(cantidadFormateada, NORMAL_FONT));

            //Acumular totales del pais
            groupTotalPrecio += Precioindividual;
            groupTotalExistencia += producto.getExistencia();
        }

        //Imprimir totales para el último pais
        if (currentOrigen != null) {
            PdfPCell totalCellLabel = new PdfPCell(new Phrase("Total del Producto del país: " + currentOrigen, boldFont));
            totalCellLabel.setColspan(4);
            totalCellLabel.setBackgroundColor(writeColor);
            table.addCell(totalCellLabel);

            PdfPCell totalExistenciaCell = new PdfPCell(new Phrase(String.valueOf(groupTotalExistencia), boldFont));
            totalExistenciaCell.setBackgroundColor(writeColor);
            table.addCell(totalExistenciaCell);

            PdfPCell totalPrecioCell = new PdfPCell(new Phrase(df.format(groupTotalPrecio), boldFont));
            totalPrecioCell.setBackgroundColor(writeColor);
            table.addCell(totalPrecioCell);
        }
    }


}
