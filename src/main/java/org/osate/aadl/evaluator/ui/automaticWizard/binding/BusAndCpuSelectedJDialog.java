package org.osate.aadl.evaluator.ui.automaticWizard.binding;

import fluent.gui.impl.swing.FluentTable;
import fluent.gui.table.CustomTableColumn;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXHeader;
import org.osate.aadl.evaluator.evolution.BusUtils;
import org.osate.aadl.evaluator.automatic.AutomaticBinding;
import org.osate.aadl.evaluator.automatic.BusAndCpu;

public class BusAndCpuSelectedJDialog extends javax.swing.JDialog 
{
    private FluentTable<BusAndCpu> table;
    private AutomaticBinding binding;
    private boolean saved;
    
    public BusAndCpuSelectedJDialog( java.awt.Window parent ) 
    {
        super( parent );
        
        initComponents();
        init();
        
        setTitle( "Bus & CPU" );
        setSize( 300 , 400 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        saved = false;
        
        add( new JXHeader( 
            "Bus & CPU" , 
            "Please, select the bus and cpu would you like to evaluate with this candidate." 
        ) , BorderLayout.NORTH );
        
        jScrollPane1.setViewportView(
            table = new FluentTable<>( "bus&cpu" ) 
        );
        
        table.addColumn( new CustomTableColumn<BusAndCpu,String>( "BUS" ){
            @Override
            public String getValue( BusAndCpu busAndCpu ) {
                return busAndCpu.getBus();
            }
        });
        
        table.addColumn( new CustomTableColumn<BusAndCpu,String>( "CPU" ){
            @Override
            public String getValue( BusAndCpu busAndCpu ) {
                return busAndCpu.getCpu();
            }
        });
        
        table.setUp();
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                table.setTabelaVaziaMensagem( "No BUS & CPU" );
                table.setTabelaVazia();
            }
        } );
    }
    
    public void setBinding( AutomaticBinding binding )
    {
        this.binding = binding;
        cpuAndBusTableFilled();
    }

    public AutomaticBinding getBinding()
    {
        binding.getBusesAndCpus().clear();
        binding.getBusesAndCpus().addAll( table.getSelectedObjects() );
        
        return binding;
    }
    
    private void cpuAndBusTableFilled()
    {
        table.setData( null );
        
        int pos = 0;
        
        for( Map.Entry<String,List<String>> entry : BusUtils.getProcessors( binding.getSystem() ).entrySet() )
        {
            String bus = entry.getKey();
            
            for( String cpu : entry.getValue() )
            {
                BusAndCpu bc = new BusAndCpu( bus , cpu );
                table.addData( bc );
                
                if( ((AutomaticBinding) binding).getBusesAndCpus().contains( bc ) )
                {
                    setSelected( pos );
                }
                
                pos++;
            }
        }
    }
    
    private void setSelected( final int pos )
    {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                table.getSelectionModel().addSelectionInterval( pos , pos + 1 );
            }
        });
    }
    
    public boolean isSaved()
    {
        return saved;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        selectJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar1.setFloatable(false);
        jToolBar1.add(filler1);

        selectJButton.setText("Select");
        selectJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        selectJButton.setFocusable(false);
        selectJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        selectJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        selectJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectJButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(selectJButton);

        cancelJButton.setText("Cancel");
        cancelJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancelJButton.setFocusable(false);
        cancelJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cancelJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(cancelJButton);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_END);
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectJButtonActionPerformed
        saved = true;
        setVisible( false );
    }//GEN-LAST:event_selectJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        saved = false;
        setVisible( false );
    }//GEN-LAST:event_cancelJButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton selectJButton;
    // End of variables declaration//GEN-END:variables
}
