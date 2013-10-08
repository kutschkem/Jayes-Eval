/*******************************************************************************
 * Copyright (c) 2013 Michael Kutschke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Kutschke - initial API and implementation
 ******************************************************************************/
package org.eclipse.recommenders.internal.jayes.eval.wiring;

import org.eclipse.recommenders.eval.jayes.apps.DiagramGeneratorApp;
import org.eclipse.recommenders.eval.jayes.apps.Evaluation;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        Object[] cmds = { new Evaluation(), new DiagramGeneratorApp() };
//        for (Object cmd : cmds)
//            Commands.registerAnnotatedCommand(context, cmd);
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

}
