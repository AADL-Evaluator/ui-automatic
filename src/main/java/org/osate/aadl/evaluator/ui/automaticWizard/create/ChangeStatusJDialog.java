package org.osate.aadl.evaluator.ui.automaticWizard.create;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import org.jdesktop.swingx.JXHeader;

public class ChangeStatusJDialog extends javax.swing.JDialog 
{
    private boolean saved;
    
    public ChangeStatusJDialog( java.awt.Window parent )
    {
        super( parent );
        
        initComponents();
        init();
        
        setTitle( "Change the Status" );
        setSize( 400 , 200 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        add( new JXHeader( 
            "Change the Status" , 
            "Please, select the status would you like to change." 
        ) , BorderLayout.NORTH );
        
        saved = false;
        
        statusJList.requestFocusInWindow();
        statusJList.setSelectedIndex( 0 );
    }
    
    public void setStatus( int status )
    {
        statusJList.setSelectedIndex( status );
    }
    
    public int getStatus()
    {
        return statusJList.getSelectedIndex();
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
        statusJList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar1.setFloatable(false);
        jToolBar1.add(filler1);

        selectJButton.setText("Select");
        selectJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        statusJList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Accepted", "Warning", "Ignored", "Deleted" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        statusJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        statusJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statusJListMouseClicked(evt);
            }
        });
        statusJList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                statusJListKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(statusJList);

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

    private void statusJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statusJListMouseClicked
        if( evt.getClickCount() >= 2 )
        {
            selectJButtonActionPerformed( null );
        }
    }//GEN-LAST:event_statusJListMouseClicked

    private void statusJListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_statusJListKeyPressed
        if( evt.getKeyCode() == KeyEvent.VK_ENTER )
        {
            selectJButtonActionPerformed( null );
        }
    }//GEN-LAST:event_statusJListKeyPressed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton selectJButton;
    private javax.swing.JList<String> statusJList;
    // End of variables declaration//GEN-END:variables
}
