/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.GraficoImpl;
import java.awt.Dimension;
import java.io.File;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import jdk.nashorn.internal.objects.annotations.Constructor;
import modelo.GraficoBarra;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

@Named(value = "GraficosC")
@SessionScoped
public class GraficosC implements Serializable {

    private GraficoBarra gra;
    private GraficoImpl dao;

    public GraficosC() {
        gra = new GraficoBarra();
        dao = new GraficoImpl();
    }

    public void grafico() throws Exception {
        int var1 = dao.Graficar();
        int var2 = dao.Graficar01();
        int var3 = dao.Graficar03();
        String venta = "Ventas";
        DefaultCategoryDataset datos = new DefaultCategoryDataset();
        datos.setValue(var1, venta, "2022-06-17");
        datos.setValue(var2, venta, "2022-05-19");
        datos.setValue(var3, venta, "2022-05-22");
        JFreeChart graficoBarras = ChartFactory.createBarChart3D("Cantidad de Ventas por fechas", "Ventas por fechas", "cantidad de ventas", datos, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel panel = new ChartPanel(graficoBarras);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(400, 200));
        try {
            ChartUtilities.saveChartAsPNG(new File("C:\\Users\\Paul\\Desktop\\Clone Software\\AS211S3_T10_COMPUTRONIK\\src\\main\\webapp\\Image\\imageJFreeChart.png"), graficoBarras, 400, 400);
        } catch (Exception e) {
            System.out.println("error en graficoC " +e.getMessage());
        }
    }

    public GraficoBarra getGra() {
        return gra;
    }

    public void setGra(GraficoBarra gra) {
        this.gra = gra;
    }

    public GraficoImpl getDao() {
        return dao;
    }

    public void setDao(GraficoImpl dao) {
        this.dao = dao;
    }
}
