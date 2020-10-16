package org.osate.aadl.evaluator.ui.automaticWizard.select;

import org.osate.aadl.evaluator.automatic.AutomaticOptions;
import org.osate.aadl.evaluator.automatic.AutomaticOption;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.osate.aadl.evaluator.evolution.FuncionalityUtils;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.ComponentPackage;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.project.Project;

public class AutomaticOptionsFinder 
{
    private final Collection<String> funcionatilies;
    private final List<AutomaticOption> options;
    private final Declaration declaration;
    private final Project project;

    public AutomaticOptionsFinder( Declaration declaration , Collection<String> funcionatilies ) 
    {
        this.options = new LinkedList<>();
        
        this.funcionatilies = funcionatilies;
        this.declaration = declaration;
        this.project = declaration.getParent().getParent().getParent();
    }
    
    public AutomaticOptions find()
    {
        for( ComponentPackage pack : project.getPackages().values() )
        {
            for( Component component : pack.getComponents().values() )
            {
                if( !FuncionalityUtils.hasOneThisFuncionalities( component , funcionatilies ) 
                    || !component.isImplementation() )
                {
                    continue ;
                }

                options.add( new AutomaticOption( component ) );
            }
        }
        
        AutomaticOptions p = new AutomaticOptions( declaration );
        p.setDeclaration( declaration );
        p.getComponents().addAll( options );
        p.getFuncionalities().addAll( funcionatilies );
        
        return p;
    }
    
}