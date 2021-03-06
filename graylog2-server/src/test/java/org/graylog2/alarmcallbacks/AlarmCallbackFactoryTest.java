/**
 * This file is part of Graylog.
 *
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graylog2.alarmcallbacks;

import org.graylog2.plugin.alarms.callbacks.AlarmCallback;
import org.graylog2.shared.bindings.InstantiationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AlarmCallbackFactoryTest {
    private AlarmCallbackFactory alarmCallbackFactory;
    @Mock
    private InstantiationService instantiationService;
    @Mock
    private DummyAlarmCallback dummyAlarmCallback;

    public interface DummyAlarmCallback extends AlarmCallback {
    }

    @Before
    public void setUp() throws Exception {
        when(instantiationService.getInstance(DummyAlarmCallback.class)).thenReturn(dummyAlarmCallback);
        Set<Class<? extends AlarmCallback>> availableAlarmCallbacks = new HashSet<Class<? extends AlarmCallback>>();
        availableAlarmCallbacks.add(DummyAlarmCallback.class);

        this.alarmCallbackFactory = new AlarmCallbackFactory(instantiationService, availableAlarmCallbacks);
    }

    @Test
    public void testCreateByAlarmCallbackConfiguration() throws Exception {
        AlarmCallbackConfiguration configuration = mock(AlarmCallbackConfiguration.class);
        when(configuration.getType()).thenReturn(DummyAlarmCallback.class.getCanonicalName());

        AlarmCallback alarmCallback = alarmCallbackFactory.create(configuration);

        assertNotNull(alarmCallback);
        assertTrue(alarmCallback instanceof DummyAlarmCallback);
        assertEquals(dummyAlarmCallback, alarmCallback);
    }

    @Test
    public void testCreateByClassName() throws Exception {
        String className = DummyAlarmCallback.class.getCanonicalName();

        AlarmCallback alarmCallback = alarmCallbackFactory.create(className);

        assertNotNull(alarmCallback);
        assertTrue(alarmCallback instanceof DummyAlarmCallback);
        assertEquals(dummyAlarmCallback, alarmCallback);
    }

    @Test
    public void testCreateByClass() throws Exception {
        AlarmCallback alarmCallback = alarmCallbackFactory.create(DummyAlarmCallback.class);

        assertTrue(alarmCallback instanceof DummyAlarmCallback);
        assertEquals(dummyAlarmCallback, alarmCallback);
    }
}
