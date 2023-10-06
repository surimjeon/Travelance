import random
from datetime import datetime, timedelta

# 계좌 관련 함수

# 계좌번호(16자리)
def generate_unique_numbers(num_of_numbers):
    unique_numbers = set()

    while len(unique_numbers) < num_of_numbers:
        new_number = str(random.randint(0, 9999999999999999)).zfill(16)  # 16자리 난수 생성
        unique_numbers.add(new_number)

    return list(unique_numbers)

# 일시
def generate_random_dates(num_dates):
    # 시작 날짜와 끝 날짜 설정
    start_date = datetime(2013, 9, 7)
    end_date = datetime(2023, 9, 7)

    # 랜덤한 날짜와 시간을 저장할 리스트
    random_date_times = list()

    for _ in range(num_dates):
        random_date = start_date + timedelta(days=random.randint(0, (end_date - start_date).days))
        random_time = datetime.now().replace(hour=random.randint(0, 23), minute=random.randint(0, 59), second=random.randint(0, 59))
        random_date_time = random_date.replace(hour=random_time.hour, minute=random_time.minute, second=random_time.second)
        random_date_times.append(random_date_time)

    return random_date_times

# 잔액(50만원에서 150만원 사이로 지정)
def generate_random_amount(again):
    # 랜덤 잔액을 저장할 리스트
    random_money = list()

    for _ in range(again):
        # 1원짜리는 제외
        num = random.randrange(500000,1500000,10)
        random_money.append(num)
    return random_money

# 비밀번호(4자리)
def generate_random_password(num_of_passwords):
    random_numbers = []

    for _ in range(num_of_passwords):
        four_digit_number = str(random.randint(0, 9999)).zfill(4)  # 0을 채워서 4자리로 만듦
        random_numbers.append(four_digit_number)

    return random_numbers

# 은행id (1 ~ 5)
def generate_random_bank(again):
    random_bank = list()

    for _ in range(again):
        num = random.randrange(1,6)
        random_bank.append(num)
    return random_bank