import random
from datetime import datetime, timedelta

# 카드 관련 함수


# 카드번호 16자리
def generate_random_card_number(num_of_numbers):
    unique_numbers = set()

    while len(unique_numbers) < num_of_numbers:
        new_number = str(random.randint(0, 9999999999999999)).zfill(16)  # 16자리 난수 생성
        unique_numbers.add(new_number)

    return list(unique_numbers)

# CVC
def generate_random_cvc(num_of_passwords):

    random_numbers = set()

    while len(random_numbers) < num_of_passwords:
        three_digit_number = str(random.randint(0, 999)).zfill(3)  # 0을 채워서 3자리로 만듦
        random_numbers.add(three_digit_number)
    return list(random_numbers)

# 유효기간
def generate_random_expirations(num_dates):
    # 시작 날짜와 끝 날짜 설정
    start_date = datetime(2026, 1, 1)
    end_date = datetime(2031, 12, 31)

    # 랜덤한 날짜와 시간을 저장할 리스트
    random_date_times = list()

    for _ in range(num_dates):
        random_date = start_date + timedelta(days=random.randint(0, (end_date - start_date).days))
        random_date_times.append(random_date)

    return random_date_times
# 카드사id(1~5)
def generate_random_card_co(again):
    random_card = list()

    for _ in range(again):
        num = random.randrange(1,6)
        random_card.append(num)
    return random_card