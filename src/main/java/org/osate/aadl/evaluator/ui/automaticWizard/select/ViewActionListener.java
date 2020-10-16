package org.osate.aadl.evaluator.ui.automaticWizard.select;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.automatic.AutomaticOptions;
import org.osate.aadl.evaluator.project.Subcomponent;

public class ViewActionListener implements ActionListener
{
    private final AutomaticSelectJPanel panel;

    public ViewActionListener( final AutomaticSelectJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        int row = panel.getTreeTable().getSelectedRow();
        if( row == -1 )
        {
            return ;
        }
        
        TreePath path = panel.getTreeTable().getPathForRow( row );
        DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) path.getLastPathComponent();
        Object object = node.getUserObject();
        
        if( object instanceof Subcomponent )
        {
            open( ((Subcomponent) object) );
        }
        else if( object instanceof Declaration )
        {
            open( ((Declaration) object).getComponent() );
        }
        else if( object instanceof Component )
        {
            open( (Component) object );
        }
        else if( object instanceof AutomaticOptions )
        {
            open( (AutomaticOptions) object );
        }
    }
    
    private void open( Component component )
    {
        CodeJDialog dialog = new CodeJDialog( 
            SwingUtilities.getWindowAncestor( panel ) 
        );
        
        dialog.setComponent( component );
        dialog.setVisible( true );
        dialog.dispose();
    }
    
    private void open( Subcomponent subcomponent )
    {
        CodeJDialog dialog = new CodeJDialog( 
            SwingUtilities.getWindowAncestor( panel ) 
        );
        
        dialog.setSubcomponent( subcomponent );
        dialog.setVisible( true );
        dialog.dispose();
    }
    
    private void open( AutomaticOptions options )
    {
        CodeJDialog dialog = new CodeJDialog( 
            SwingUtilities.getWindowAncestor( panel ) 
        );
        
        dialog.setOptions( options );
        dialog.setVisible( true );
        dialog.dispose();
    }
    
}
