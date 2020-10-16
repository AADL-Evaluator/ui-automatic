package org.osate.aadl.evaluator.ui.automaticWizard.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class DeleteActionListener implements ActionListener
{
    private final AutomaticBindingListJPanel panel;
    
    public DeleteActionListener( AutomaticBindingListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        if( panel.getTreeTable().getSelectedRow() == -1 )
        {
            return ;
        }
        
        int[] rows = panel.getTreeTable().getSelectedRows();
        
        for( int i = rows.length ; i > 0 ; i-- )
        {
            TreePath path = panel.getTreeTable().getPathForRow( rows[ i - 1 ] );
            
            panel.getTreeTableModel().removeNodeFromParent( 
                (DefaultMutableTreeTableNode) path.getLastPathComponent() 
            );
        }
    }
    
}