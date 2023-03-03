package beachcombine.backend.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.lang.Math.min;

@Component
public class RayCastingAlgorithmService {

    // 점 (x, y)가 다각형 내부에 있는지 판별하는 메서드
    public boolean isInsidePolygon(List<BigDecimal> xCoords, List<BigDecimal> yCoords, BigDecimal x, BigDecimal y) {

        // 정확도를 위해 double로 계산
        // 광선은 왼쪽에서 쏜다고 가정
        int nPoints = xCoords.size();
        System.out.println(">>>> xCoords.size():"+ nPoints);
        int nIntersections = 0;

        for (int i = 0; i < nPoints; i++) {
            BigDecimal x1 = xCoords.get(i);
            BigDecimal y1 = yCoords.get(i);
            BigDecimal x2 = xCoords.get((i + 1) % nPoints);
            BigDecimal y2 = yCoords.get((i + 1) % nPoints);

            BigDecimal minNum = (x1.compareTo(x2)<=0)? x1:x2; // x1<=x2
            if(y1.equals(y2) && y.equals(y1) && minNum.compareTo(x)<=0 ){ // minNum <=x
                return true;
            }
            if (isIntersect(x1, y1, x2, y2, x, y)) {
                nIntersections++;
            }
        }
        return nIntersections % 2 == 1;
    }

    // 선분 (x1, y1) -> (x2, y2)와 반직선 y = y0가 교차하는지 판별하는 메서드
    private boolean isIntersect(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2, BigDecimal x0, BigDecimal y0) {

        // 선분의 y 좌표값이 모두 y0보다 작거나 모두 y0보다 큰 경우 교차하지 않는다.
        // (y0-y1)*(y0-y2)>0
        BigDecimal result = (y0.subtract(y1)).multiply(y0.subtract(y2));
        if (result.compareTo(BigDecimal.ZERO)>0) {
            return false;
        }

        // 선분의 기울기가 0일 경우 (y0==y1==y2) --> x값 비교 필요, 함수 호출 전에 이미 검증함
        if(y1.equals(y2)){
           return false; // x값 범위 안맞을 때
        }


        // 선분의 기울기가 0이 아닌 경우, 선분의 x 좌표값을 구한다.
        // (y0 - y1) * (x2 - x1) / (y2 - y1) + x1
        BigDecimal x =  (((y0.subtract(y1)).multiply(y2.subtract(y1))).divide(y2.subtract(y1), 20, RoundingMode.HALF_UP)).add(x1);

        // x 좌표값이 범위 안에 있는지 확인
        // x 좌표는 이미 (x-x1)(x-x2)<=0 만족해서 확인필요 없음
        return x.compareTo(x0) <= 0; // x<=x0
    }
}
