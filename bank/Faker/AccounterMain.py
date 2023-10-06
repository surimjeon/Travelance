from faker import Faker
import uuid
import mysql.connector
from Account import generate_unique_numbers, generate_random_dates, generate_random_amount, generate_random_password, generate_random_bank
from Card import generate_random_card_number, generate_random_cvc, generate_random_expirations, generate_random_card_co  

fake = Faker('ko_KR')

# DB 연결(일단은 서버로 지정)
conn = mysql.connector.connect(
    host='3.39.110.134',
    port=3306,
    user='root',
    password='qwe123',   
    database='bank'
)

# # DB 연결(로컬데이터로 지정)
# conn = mysql.connector.connect(
#     host='localhost',
#     port=3300,
#     user='root',
#     password='1234',
#     database='bank'
# )

# 사용자 관련
accounter_info = {
    # 사용자 테이블 이름
    'accounter_table': 'accounter',
    # 테이블 아이디
    'accounter_id': 'accounter_id',
    # 이름 컬럼
    'name': 'name',
    # 개인 고유번호 컬럼
    'private_id': 'private_id'
}
# 계좌 관련
account_info = {
    # 계좌 테이블
    'bank_account' : 'bankaccount',
    # 계좌 번호
    'account' : 'account',
    # 생성일
    'created_at' : 'created_at',
    # 잔액
    'balance' : 'balance',
    # 계좌 비밀번호
    'account_password' : 'account_password',
    # 은행 id
    'bank_id' : 'bank_name_id'
}
#카드 관련
card_info = {
    # 카드 테이블
    'bank_card' : 'bankcard',
    # 카드 번호
    'card_number' : 'card_number',
    # 카드 비밀번호
    'card_password' : 'card_password',
    # CVC코드
    'cvc' : 'cvc',
    # 유효기간
    'expiration' : 'date',
    # 카드사id
    'card_co_id' : 'card_co_name_id'
}

# 은행사 관련
bank = [
    # 은행명, 은행로고, 은행코드
    ['SC제일','img/bank/001_SC제일은행','001'],
    ['KB국민','img/bank/002_국민은행','002'],
    ['IBK기업','img/bank/003_기업은행','003'],
    ['NH농협','img/bank/004_농협','004'],
    ['대구','img/bank/005_대구은행','005'],
    ['KDB산업','img/bank/006_산업은행','006'],
    ['새마을','img/bank/007_새마을금고','007'],
    ['신한','img/bank/008_신한은행','008'],
    ['우리','img/bank/009_우리은행','009'],
    ['하나','img/bank/010_하나은행','010'],
]

# 카드사 관련
card = [
    # 카드사명, 카드로고, 카드사코드
    ['BC','img/card/001_BC카드','001'],
    ['KB국민','img/card/002_국민카드','002'],
    ['NH농협','img/card/003_농협','003'],
    ['롯데','img/card/004_롯데카드','004'],
    ['삼성','img/card/005_삼성카드','005'],
    ['신한','img/card/006_신한카드','006'],
    ['하나','img/card/007_하나카드','007'],
    ['현대','img/card/008_현대카드','008'],

]


cursor = conn.cursor()

# 뱅크 이용자 관련 함수
# 혹시 몰라 DB에서도 고유번호 중복 체크
def check_id_duplicate_in_db(id):
    cursor.execute(f"SELECT COUNT(*) FROM {accounter_info['accounter_table']} WHERE {accounter_info['private_id']} = %s", (str(id),))
    count = cursor.fetchone()[0]
    return count > 0

def generate_unique_id(unique_id):
    # 지금까지 만든 곳에서나 DB에서 중복이 있으면
    while check_id_duplicate(unique_id) or check_id_duplicate_in_db(unique_id):
        # uuid1로 새 값주기
        unique_id = uuid.uuid1()
    return unique_id
# 중복 체크
def check_id_duplicate(id):
    return id in total


def create_bank_account():
    name = "은행"
    name_uuid = uuid.uuid5(uuid.NAMESPACE_DNS, name)
    unique_id = generate_unique_id(name_uuid)

    cursor.execute(f"INSERT INTO {accounter_info['accounter_table']} ({accounter_info['name']}, {accounter_info['private_id']}) VALUES (%s, %s)", (name, str(unique_id)))
    conn.commit()

    # 마지막으로 저장한 사람의 PK값 가져오기
    cursor.execute("SELECT LAST_INSERT_ID()")
    last_inserted_id = cursor.fetchone()[0]


    cursor.execute(f"INSERT INTO {account_info['bank_account']} ({accounter_info['accounter_id']}, {account_info['account']}, {account_info['created_at']}, {account_info['balance']}, {account_info['account_password']}, {account_info['bank_id']}) VALUES (%s, %s, %s, %s, %s, %s)",
            (last_inserted_id, generate_unique_numbers(1)[0], generate_random_dates(1)[0].strftime('%Y-%m-%d %H:%M:%S'), 999999999, generate_random_password(1)[0], 1))
    conn.commit()

    print("은행계좌 생성 완료")

# 생성할 반복 횟수
new = 30

# 생성된 개인고유번호
total = list()

# 생성된 계좌번호 리스트
unique_numbers = generate_unique_numbers(new)
# 생성된 날짜들
random_dates = generate_random_dates(new)
# 생성된 잔액들
random_amount =  generate_random_amount(new)
# 생성된_비밀번호
random_password = generate_random_password(new)
# 생성된_은행id
random_bank_id = generate_random_bank(new)

# 생성된 카드번호 리스트
random_cards = generate_random_card_number(new)
# 생성된 카드비밀번호
random_card_passwords = generate_random_password(new)
# 생성된 CVC
random_cvcs = generate_random_cvc(new)
# 생성된 유효기간
random_expirations = generate_random_expirations(new)
#생성된 카드사id
random_card_co = generate_random_card_co(new)

# 사용자 지정
user = 10

# 사용자별 계좌 개수
account_cnt = 0
account_plus = 3

# 사용자별 카드 개수
card_cnt = 0
card_plus = 3



# 은행 더미데이터 생성
for i in range(len(bank)):
    cursor.execute("INSERT INTO bankname (bank_name, idx ,bank_code) VALUES (%s, %s, %s)", (bank[i][0], i, bank[i][2]))
    conn.commit()
print('은행 더미 데이터 생성완료')
# 카드사 더미데이터 생성
for i in range(len(card)):
    cursor.execute("INSERT INTO cardconame (card_co_name, idx, card_co_code) VALUES (%s, %s, %s)", (card[i][0], i, card[i][2]))
    conn.commit()
print('카드사 더미 데이터 생성완료')


create_bank_account()

name_list = ['김제준','전수림','조혜진','하동혁','황신운','차선호','Mike','Jackson','Denver','Carroll']

# 뱅크사용자를 몇 명 만들건지
for t in range(user):
    # name = fake.name()
    name = name_list[t]
    name_uuid = uuid.uuid5(uuid.NAMESPACE_DNS, name)
    total.append(name_uuid)
    unique_id = generate_unique_id(name_uuid)

    # 랜덤 계좌번호
    account = list()

    # 데이터를 MySQL 데이터베이스에 저장합니다.
    cursor.execute(f"INSERT INTO {accounter_info['accounter_table']} ({accounter_info['name']}, {accounter_info['private_id']}) VALUES (%s, %s)", (name, str(unique_id)))
    conn.commit()

    # 마지막으로 저장한 사람의 PK값 가져오기
    cursor.execute("SELECT LAST_INSERT_ID()")
    last_inserted_id = cursor.fetchone()[0]

    # 세개씩 저장
    # 사용자 id, 계좌번호, 생성일, 잔액, 계좌비밀번호, 은행id
    for i in range(account_cnt,account_cnt + account_plus):
        cursor.execute(f"INSERT INTO {account_info['bank_account']} ({accounter_info['accounter_id']}, {account_info['account']}, {account_info['created_at']}, {account_info['balance']}, {account_info['account_password']}, {account_info['bank_id']}) VALUES (%s, %s, %s, %s, %s, %s)",
               (last_inserted_id, unique_numbers[i], random_dates[i].strftime('%Y-%m-%d %H:%M:%S'), random_amount[i], random_password[i], random_bank_id[i]))
        conn.commit()
    
    for i in range(card_cnt,card_cnt + card_plus):
        cursor.execute(f"INSERT INTO {card_info['bank_card']} ({accounter_info['accounter_id']}, {card_info['card_number']}, {card_info['card_password']}, {card_info['cvc']}, {card_info['expiration']},{card_info['card_co_id']}) VALUES (%s, %s, %s, %s, %s, %s)",
               (last_inserted_id, random_cards[i], random_card_passwords[i], random_cvcs[i], random_expirations[i].strftime('%Y-%m-%d'), random_card_co[i]))
        conn.commit()

    account_cnt += account_plus
    card_cnt += card_plus

    print(name,unique_id, '생성완료')



conn.close()

    