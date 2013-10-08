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
package org.eclipse.recommenders.eval.jayes.statistics;

import java.util.Map;

import org.eclipse.recommenders.eval.jayes.statistics.memory.JunctionTreeMemoryStatisticsProvider;

import com.google.inject.ImplementedBy;

@ImplementedBy(JunctionTreeMemoryStatisticsProvider.class)
public interface IStatisticsProvider {

    Map<String, Number> computeStatistics();

}
