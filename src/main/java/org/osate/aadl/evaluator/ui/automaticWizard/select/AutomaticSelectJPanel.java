package org.osate.aadl.evaluator.ui.automaticWizard.select;

import org.osate.aadl.evaluator.automatic.AutomaticOptions;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.osate.aadl.evaluator.project.Component;

public class AutomaticSelectJPanel extends javax.swing.JPanel 
{
    private final Executor executor;
    private Component system;
    
    private CandidateTreeTableModel treeTableModel;
    private JXTreeTable treeTable;
    
    public AutomaticSelectJPanel() 
    {
        this.executor = Executors.newSingleThreadExecutor();
        
        initComponents();
        init();
    }
    
    private void init()
    {
        initTable();
        
        viewJButton.addActionListener( new ViewActionListener( this ) );
    }
    
    private void initTable()
    {
        jScrollPane1.setViewportView( 
            treeTable = new JXTreeTable(
                treeTableModel = new CandidateTreeTableModel()
            ) 
        );
        
        treeTableModel.setRoot( 
            new DefaultMutableTreeTableNode( "System" ) 
        );
    }
    
    // ----------------------- //
    // ----------------------- //
    // ----------------------- //

    public void setSystem( Component system )
    {
        this.system = system;
        this.executor.execute( new AutomaticOptionsRunnable( this ) );
    }

    public Component getSystem()
    {
        return system;
    }

    public JXTreeTable getTreeTable() 
    {
        return treeTable;
    }
    
    public CandidateTreeTableModel getTreeTableModel() 
    {
        return treeTableModel;
    }
    
    public List<AutomaticOptions> getSelected()
    {
        return treeTableModel.getSelected();
    }
    
    public void setMessage( String message )
    {
        JLabel label = new JLabel( message );
        label.setFont( new Font( Font.MONOSPACED , Font.BOLD , 30 ) );
        label.setHorizontalAlignment( JLabel.CENTER );
        label.setHorizontalTextPosition( JLabel.CENTER );
        
        JPanel panel = new JPanel( new BorderLayout() );
        panel.add( label , BorderLayout.CENTER );
        panel.setBackground( Color.WHITE );
        
        jScrollPane1.setViewportView( panel );
    }
    
    public void setRoot( DefaultMutableTreeTableNode root )
    {
        jScrollPane1.setViewportView( treeTable );
        treeTableModel.setRoot( root );
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                treeTable.getColumnModel().getColumn( 1 ).setMaxWidth( 100 );
                treeTable.getColumnModel().getColumn( 2 ).setMaxWidth( 80 );
                treeTable.getColumnModel().getColumn( 3 ).setMaxWidth( 80 );
            }
        });
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                treeTable.getSelectionModel().setSelectionInterval( 0 , 0 );
            }
        });
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                treeTable.expandRow( 0 );
            }
        });
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        viewJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);

        jLabel1.setText("System");
        jToolBar1.add(jLabel1);
        jToolBar1.add(filler1);

        viewJButton.setText("View");
        viewJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        viewJButton.setFocusable(false);
        viewJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(viewJButton);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton viewJButton;
    // End of variables declaration//GEN-END:variables
}
