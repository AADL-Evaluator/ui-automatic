package org.osate.aadl.evaluator.ui.automaticWizard.create;

import fluent.gui.impl.swing.FluentTable;
import fluent.gui.table.CustomTableColumn;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.osate.aadl.evaluator.evolution.EvolutionUtils;
import org.osate.aadl.evaluator.automatic.AutomaticEvolution;
import org.osate.aadl.evaluator.automatic.AutomaticOptions;
import org.osate.aadl.evaluator.ui.mainWizard.AadlComponentRunnable;
import org.osate.aadl.evaluator.ui.mainWizard.AadlDetailsRunnable;

public class AutomaticEvolutionListJPanel extends javax.swing.JPanel
{
    private final Executor executor;
    
    private final List<AutomaticEvolution> evolutions;
    private FluentTable<AutomaticEvolution> table;
    private List<AutomaticOptions> options;
    
    public AutomaticEvolutionListJPanel()
    {
        executor = Executors.newSingleThreadExecutor();
        evolutions = new LinkedList<>();
        
        initComponents();
        init();
        
        jTabbedPane1.remove( 1 );
    }
    
    private void init()
    {
        initTable();
        
        statusJComboBox.addItemListener( new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if( e.getStateChange() != ItemEvent.SELECTED ){
                    return ;
                }
                
                reorder();
            }
        });
        
        detailJTextArea.setFont( new Font( Font.MONOSPACED , Font.PLAIN , 13 ) );
        viewJTextArea  .setFont( new Font( Font.MONOSPACED , Font.PLAIN , 13 ) );
        
        statusJButton.addActionListener( new ChangeStatusActionListener( this ) );
    }
    
    private void initTable()
    {
        jScrollPane1.setViewportView( 
            table = new FluentTable<>( "evolutions" ) 
        );
        
        table.addColumn( new CustomTableColumn<AutomaticEvolution,String>( "Change #" ){
            @Override
            public String getValue( AutomaticEvolution evolution ) {
                return "Change Nº " + evolution.getNumber();
            }
        });
        
        table.addColumn( new CustomTableColumn<AutomaticEvolution,String>( "status" ){
            @Override
            public String getValue( AutomaticEvolution ae ) {
                return ae.getStatusString();
            }
        });
        
        table.addColumn( new CustomTableColumn<AutomaticEvolution,String>( "Message" ){
            @Override
            public String getValue( AutomaticEvolution evolution ) {
                return EvolutionUtils.toString( evolution.getMessages() );
            }
        });
        
        table.setUp();
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                table.setTabelaVaziaMensagem( "No evolution was created." );
                table.setTabelaVazia();
            }
        });
        
        table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged( ListSelectionEvent e ) {
                detailJTextArea.setText( "" );
                viewJTextArea.setText( "" );
                
                if( table.getSelectedRow() == -1 )
                {
                    return ;
                }
                
                AutomaticEvolution evolution = table.getSelectedObject();
                executor.execute( new AadlDetailsRunnable( detailJTextArea, evolution ) );
                
                try
                {
                    executor.execute( new AadlComponentRunnable( 
                        viewJTextArea , 
                        evolution , 
                        evolution.getSystemWidthChanges()
                    ) );
                }
                catch( Exception err )
                {
                    err.printStackTrace();
                }
                
                /*
                executor.execute( 
                    new AnalysisRunnable( 
                        analysisTable , 
                        new EvolutionReport( "original"  ).setEvolution( evolution ) , 
                        new EvolutionReport( "evolution" ).setEvolution( evolution )
                    ) 
                );
                */
            }
        });
    }
    
    public void reorder()
    {
        executor.execute( new Runnable() {
            @Override
            public void run() {
                int status = statusJComboBox.getSelectedIndex();
                
                if( status == 4 ){
                    table.setData( evolutions );
                }
                else {
                    table.setData( null );
                    
                    for( AutomaticEvolution e : evolutions ){
                        if( e.getStatus() == status ){
                            table.addData( e );
                        }
                    }
                }
            }
        });
    }

    // ----------------------- //
    // ----------------------- //
    // ----------------------- //
    
    public void setOptions( List<AutomaticOptions> options )
    {
        this.options = options;
        this.executor.execute( new AutomaticCreateRunnable( this ) );
    }

    public List<AutomaticOptions> getOptions()
    {
        return options;
    }
    
    public void setEvolutions( List<AutomaticEvolution> evolutions )
    {
        this.evolutions.clear();
        this.evolutions.addAll( evolutions );
        
        statusJComboBox.setSelectedIndex( statusJComboBox.getItemCount() - 1 );
    }

    public FluentTable<AutomaticEvolution> getTable()
    {
        return table;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        detailJTextArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        viewJTextArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jLabel2 = new javax.swing.JLabel();
        statusJComboBox = new javax.swing.JComboBox<>();
        statusJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(300);

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        detailJTextArea.setEditable(false);
        detailJTextArea.setColumns(20);
        detailJTextArea.setRows(5);
        jScrollPane2.setViewportView(detailJTextArea);

        jTabbedPane1.addTab("Change Details", jScrollPane2);

        viewJTextArea.setEditable(false);
        viewJTextArea.setColumns(20);
        viewJTextArea.setRows(5);
        jScrollPane3.setViewportView(viewJTextArea);

        jTabbedPane1.addTab("Change View", jScrollPane3);

        jSplitPane1.setRightComponent(jTabbedPane1);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);

        jLabel1.setText("Evolutions");
        jToolBar1.add(jLabel1);
        jToolBar1.add(filler1);

        jLabel2.setText("status: ");
        jToolBar1.add(jLabel2);

        statusJComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Accepted", "Warning", "Ignored", "Deleted", "All" }));
        jToolBar1.add(statusJComboBox);

        statusJButton.setText("status");
        statusJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        statusJButton.setFocusable(false);
        statusJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        statusJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(statusJButton);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.PAGE_START);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        if( evt.getClickCount() >= 3 )
        {
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run() {
                    if( jTabbedPane1.getTabCount() == 1 )
                    {
                        jTabbedPane1.add( "Change View" , jScrollPane3 );
                    }
                    else
                    {
                        jTabbedPane1.remove( 1 );
                    }
                }
            });
        }
    }//GEN-LAST:event_jTabbedPane1MouseClicked
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea detailJTextArea;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton statusJButton;
    private javax.swing.JComboBox<String> statusJComboBox;
    private javax.swing.JTextArea viewJTextArea;
    // End of variables declaration//GEN-END:variables
}
