package org.osate.aadl.evaluator.ui.automaticWizard.binding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.osate.aadl.evaluator.evolution.BindingUtils;
import org.osate.aadl.evaluator.automatic.AutomaticBinding;
import org.osate.aadl.evaluator.automatic.AutomaticOption;
import org.osate.aadl.evaluator.automatic.AutomaticOptions;

public class AutomaticBindingListJPanel extends javax.swing.JPanel 
{
    private final Executor executor;
    
    private OptionsTreeTableModel treeTableModel;
    private JXTreeTable treeTable;
    private List<AutomaticOptions> options;
    
    public AutomaticBindingListJPanel() 
    {
        this.executor = Executors.newSingleThreadExecutor();
        
        initComponents();
        init();
    }
    
    private void init()
    {
        treeTable = new JXTreeTable( 
            treeTableModel = new OptionsTreeTableModel() 
        );
        
        treeTableModel.setRoot( new DefaultMutableTreeTableNode( "Options" ) );
        
        treeTable.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed( KeyEvent e ) {
                if( e.getKeyCode() == KeyEvent.VK_DELETE )
                {
                    deleteJButton.doClick();
                }
            }
        });
        
        editJButton.addActionListener( new EditActionListener( this ) );
        busAndCpuJButton.addActionListener( new EditBusAndCpuActionListener( this ) );
        detailsJButton.addActionListener( new DetailsActionListener( this ) );
        deleteJButton.addActionListener( new DeleteActionListener( this ) );
    }
    
    // ---------------------
    // ---------------------
    // ---------------------
    
    public void setMessage( String message )
    {
        JLabel label = new JLabel( message );
        label.setFont( new Font( Font.MONOSPACED , Font.BOLD , 30 ) );
        label.setHorizontalAlignment( JLabel.CENTER );
        label.setHorizontalTextPosition( JLabel.CENTER );
        
        JPanel panel = new JPanel( new BorderLayout() );
        panel.add( label , BorderLayout.CENTER );
        panel.setBackground( Color.WHITE );
        
        tableJScrollPane.setViewportView( panel );
    }
    
    public void showTreeTable()    
    {
        tableJScrollPane.setViewportView( treeTable );
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                treeTable.expandAll();
            }
        });
    }
    
    public JXTreeTable getTreeTable()
    {
        return treeTable;
    }
    
    public OptionsTreeTableModel getTreeTableModel() 
    {
        return treeTableModel;
    }
    
    // ---------------------
    // ---------------------
    // ---------------------
    
    public AutomaticBindingListJPanel setOptions( List<AutomaticOptions> options )
    {
        this.options = options;
        
        this.setMessage( "Processing the connections..." );
        this.executor.execute( new TreeTableRunnable( this ) );
        
        return this;
    }
    
    public List<AutomaticOptions> getOptions() 
    {
        return options;
    }
    
    public List<AutomaticOptions> getOptionsFromView() 
    {
        List<AutomaticOptions> selected = new LinkedList<>();
        
        for( int i = 0 ; i < treeTableModel.getRoot().getChildCount() ; i++ )
        {
            selected.add( getOptions( 
                (DefaultMutableTreeTableNode) treeTableModel.getRoot().getChildAt( i )
            ) );
        }
        
        return selected;
    }
    
    public AutomaticOptions getOptions( DefaultMutableTreeTableNode node )
    {
        final AutomaticOptions ops = ((AutomaticOptions) node.getUserObject()).clone();
        ops.getComponents().clear();

        for( int i = 0 ; i < node.getChildCount() ; i++ )
        {
            ops.getComponents().add( 
                getOption( (DefaultMutableTreeTableNode) node.getChildAt( i ) ) 
            );
        }
        
        return ops;
    }
    
    public AutomaticOption getOption( DefaultMutableTreeTableNode node )
    {
        final AutomaticOption op = ((AutomaticOption) node.getUserObject()).clone();
        op.getBindings().clear();

        for( int i = 0 ; i < node.getChildCount() ; i++ )
        {
            TreeTableNode n = node.getChildAt( i );
            
            op.getBindings().add( new LinkedList<>() );
            
            for( int j = 0 ; j < n.getChildCount() ; j++ )
            {
                AutomaticBinding ab = (AutomaticBinding) ((DefaultMutableTreeTableNode) 
                    n.getChildAt( j ))
                    .getUserObject();

                if( BindingUtils.isAllPartsWereSetted( ab ) )
                {
                    op.getBindings().get( i ).add( ab );
                }
            }
        }
        
        return op;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        editJButton = new javax.swing.JButton();
        busAndCpuJButton = new javax.swing.JButton();
        detailsJButton = new javax.swing.JButton();
        deleteJButton = new javax.swing.JButton();
        tableJScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);

        jLabel1.setText("Interfaces & Connections");
        jToolBar1.add(jLabel1);
        jToolBar1.add(filler1);

        editJButton.setText("Edit");
        editJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editJButton.setFocusable(false);
        editJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(editJButton);

        busAndCpuJButton.setText("Bus & CPU");
        busAndCpuJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        busAndCpuJButton.setFocusable(false);
        busAndCpuJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        busAndCpuJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(busAndCpuJButton);

        detailsJButton.setText("Details");
        detailsJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        detailsJButton.setFocusable(false);
        detailsJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        detailsJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(detailsJButton);

        deleteJButton.setText("Delete");
        deleteJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteJButton.setFocusable(false);
        deleteJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(deleteJButton);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
        add(tableJScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton busAndCpuJButton;
    private javax.swing.JButton deleteJButton;
    private javax.swing.JButton detailsJButton;
    private javax.swing.JButton editJButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JScrollPane tableJScrollPane;
    // End of variables declaration//GEN-END:variables
}
