package org.osate.aadl.evaluator.ui.automaticWizard.create;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.osate.aadl.evaluator.automatic.AutomaticEvolution;
import org.osate.aadl.evaluator.automatic.AutomaticOptions;

public class AutomaticCreateRunnable implements Runnable
{
    private final AutomaticEvolutionListJPanel panel;
    
    public AutomaticCreateRunnable( AutomaticEvolutionListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void run()
    {
        System.out.println( "[AUTOMATIC EVOLUTION CREATOR]" );
        
        // ------ STEP 1: create all AutomaticEvolution for individual Options
        Collection<List<AutomaticEvolution>> evolutions = step1_create();
        
        // ------ STEP 2: compose de map
        List<AutomaticEvolution> results = step2_compose( evolutions );
        
        // ------ STEP 3: set a number to all evolutions
        step3_setNumber( results );
        
        panel.setEvolutions( results );
    }
    
    private Collection<List<AutomaticEvolution>> step1_create()
    {
        Map<AutomaticOptions,List<AutomaticEvolution>> evolutions = new HashMap<>();
        
        for( AutomaticOptions options : panel.getOptions() )
        {
            evolutions.put( 
                options , 
                new AutomaticCreateStep1( options )
                .createAllEvolutions() 
            );
        }
        
        return evolutions.values();
    }
    
    private List<AutomaticEvolution> step2_compose( Collection<List<AutomaticEvolution>> all )
    {
        List<AutomaticEvolution> results = new LinkedList<>();
        
        for( List<AutomaticEvolution> l : all )
        {
            results = step2_compose( results , l );
        }
        
        return results;
    }
    
    private List<AutomaticEvolution> step2_compose( List<AutomaticEvolution> before , List<AutomaticEvolution> l )
    {
        if( before.isEmpty() )
        {
            return l;
        }
        
        List<AutomaticEvolution> results = new LinkedList<>();
        
        for( AutomaticEvolution evolution : l )
        {
            AnalysisManager.analysis( evolution );
            
            for( AutomaticEvolution old : before )
            {
                AutomaticEvolution cloned = old.clone();
                
                if( evolution.getStatus() != AutomaticEvolution.STATUS_ACCEPTED )
                {
                    cloned.setStatus( evolution.getStatus() );
                }
                
                cloned.getMessages().addAll( evolution.getMessages() );
                
                cloned.getDeclarations().addAll( evolution.getDeclarations() );
                cloned.getComponents().putAll( evolution.getComponents() );
                cloned.getCandidates().addAll( evolution.getCandidates() );
                cloned.getBindings().addAll( evolution.getBindings() );
                
                results.add( cloned );
            }
        }
        
        return results;
    }
    
    private List<AutomaticEvolution> step3_setNumber( List<AutomaticEvolution> l )
    {
        for( int i = 0 ; i < l.size() ; i++ )
        {
            l.get( i ).setNumber( i + 1 );
        }
        
        return l;
    }
    
}
