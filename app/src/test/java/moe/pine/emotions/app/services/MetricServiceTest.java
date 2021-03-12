package moe.pine.emotions.app.services;

import moe.pine.emotions.log.AvatarType;
import moe.pine.emotions.log.AvatarUpdatedRepository;
import moe.pine.emotions.mackerel.Mackerel;
import moe.pine.emotions.mackerel.Metric;
import moe.pine.emotions.app.properties.MackerelProperties;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MetricServiceTest {
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Tokyo");
    private static final LocalDateTime NOW = LocalDateTime.of(2019, 5, 15, 15, 9);
    private static final Clock CLOCK = Clock.fixed(NOW.atZone(ZONE_ID).toInstant(), ZONE_ID);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private AvatarUpdatedRepository avatarUpdatedRepository;

    @Mock
    private Mackerel mackerel;

    @Mock
    private MackerelProperties mackerelProperties;

    private MetricService metricService;

    @Before
    public void setUp() {
        metricService = new MetricService(
            avatarUpdatedRepository,
            mackerel,
            mackerelProperties,
            CLOCK,
            ZONE_ID
        );
    }

    @Test
    public void logTest() {
        doNothing().when(avatarUpdatedRepository).set(AvatarType.GRAVATAR, NOW);

        metricService.log(AvatarType.GRAVATAR);

        verify(avatarUpdatedRepository).set(AvatarType.GRAVATAR, NOW);
    }

    @Test
    public void collectTest() {
        final List<AvatarType> avatarTypes = Arrays.asList(AvatarType.values());
        final List<Pair<AvatarType, LocalDateTime>> values =
            List.of(
                Pair.of(AvatarType.GRAVATAR, LocalDateTime.of(2019, 5, 15, 1, 2)),
                Pair.of(AvatarType.SLACK, LocalDateTime.of(2019, 5, 15, 8, 42)),
                Pair.of(AvatarType.TWITTER, LocalDateTime.of(2019, 5, 15, 15, 9))
            );

        final MackerelProperties.Graph graph = new MackerelProperties.Graph("graph1");
        final MackerelProperties.Graphs graphs = new MackerelProperties.Graphs(graph);

        when(avatarUpdatedRepository.mget(avatarTypes)).thenReturn(values);
        when(mackerelProperties.getGraphs()).thenReturn(graphs);

        final List<Metric> metrics = metricService.collect();
        assertEquals(3, metrics.size());

        assertEquals("graph1.gravatar", metrics.get(0).getName());
        assertEquals(CLOCK.instant().getEpochSecond(), metrics.get(0).getTime());
        assertEquals((14 * 60 + 7) * 60, metrics.get(0).getValue().intValue());

        assertEquals("graph1.slack", metrics.get(1).getName());
        assertEquals(CLOCK.instant().getEpochSecond(), metrics.get(1).getTime());
        assertEquals((6 * 60 + 27) * 60, metrics.get(1).getValue().intValue());

        assertEquals("graph1.twitter", metrics.get(2).getName());
        assertEquals(CLOCK.instant().getEpochSecond(), metrics.get(2).getTime());
        assertEquals(0, metrics.get(2).getValue().intValue());
    }

    @Test
    public void sendTest() {
        final List<Metric> metrics =
            List.of(new Metric("metric-1", 1L, BigDecimal.ONE));

        doNothing().when(mackerel).send(metrics);

        metricService.send(metrics);

        verify(mackerel).send(metrics);
    }
}
