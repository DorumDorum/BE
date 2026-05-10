# k6 Load Testing

This directory is reserved for k6 load-test assets. Test scripts are intentionally not included.

Place scripts under `load-testing/k6/` when needed, then run:

```sh
docker compose --profile loadtest run --rm k6 run --tag testid=gc-baseline /scripts/<script>.js
```

Defaults:

- The application target for scripts is exposed as `BASE_URL=http://backend:8080`.
- k6 metrics are remote-written to Prometheus at `http://prometheus:9090/api/v1/write`.
- The optional k6 web dashboard export path is `/results/k6-report.html`.
- Use a different `testid` tag for each GC tuning experiment to separate runs in Prometheus/Grafana.
