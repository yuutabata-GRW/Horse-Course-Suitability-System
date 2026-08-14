# データベース設計

## 1.データベース概要
本システムでは、競走馬・レース・各馬の出走結果を管理する。

MySQLをシステムの正規データーベースとして使用し、
馬のプロフィールや過去のレース結果など、分析の元となるデータを保存する。

保存されたデータは、将来的にSpring Batchによる集計処理や
Elasticsearchによる検索・分析に利用する。

## 2.テーブル一覧

| テーブル名 | 説明 |  
|---|---|  
| horses | 競走馬のプロフィールを管理する |  
| races | レースそのものの情報を管理する |  
| race_entries | 各レースに出走した馬の情報・結果を管理する |

## 3.horsesテーブル
競走馬のプロフィール情報を管理する

| カラム名 | MySQL型 |  NULL | 制約 | 説明 |  
|---|---|---|---|---|  
| id | BIGINT | NO | PK, AUTO_INCREMENT | 競走馬を識別するID |  
| name | VARCHAR(18) | NO |  | 馬名 |  
| birth_date | DATE | NO |  | 生年月日 |  
| sex | VARCHAR(10) | NO |  | 性別 |

### 設計方針
- `id`はMySQLでは`BIGINT`を使用し、Javaでは`Long`として扱う。
- `name`は最大18文字とする。(正しくはカタカナ2-9文字、英字18文字以内)
- 馬名の文字種や文字数などの入力ルールは、Spring Validationでチェックする。
- `birth_date`は生年月日のみを扱うため、'DATE'を使用する。
- `sex`は現時点では文字列として管理し、将来的にEnumなどに変更を検討する。
- 脚質は固定された属性ではないため、`horses`には保持しない。`race_entries`でレースごと管理する。

## 4.racesテーブル
レースそのものの情報を管理する

| カラム名 | MySQL型 |  NULL | 制約 | 説明 |  
|---|---|---|---|---|  
| id | BIGINT |  NO | PK, AUTO_INCREMENT | レースを識別するID |  
| race_date| DATE | NO |  | レース開催日 |  
| venue | VARCHAR(50) | NO |  | 競馬場名 |  
| surface | VARCHAR(20) | NO |  | 芝/ダート |  
| distance | INT | NO |  | レース距離(単位はメートル) |  
| track_condition | VARCHAR(20) | NO |  | 馬場状態 |  
| weather | VARCHAR(20) | NO |  | 天候 |

### 設計方針
- `id`はMySQLでは`BIGINT`を使用し、Javaでは`Long`として扱う。
- `race_date`はレース開催日のみを管理するため、`DATE`を使用する。
- `venue`は競馬場名を文字列として管理する。
- `surface`は芝・ダートなどのコース種別を管理する。
- `distance`はメートル単位の整数値として管理する。
- `track_condition`は良・稍重・重・不良などの馬場状態を管理する。
- `weather`は晴・曇・雨・雪などの天候を管理する。
- 現時点では競馬場や馬場状態などをマスターテーブルには分離せず、文字列として管理する。
- 将来的に中央競馬・地方競馬のデータ量や検索要件が増えた場合は、マスターテーブル化を検討する。

## 5.race_entriesテーブル
各レースに出走した競走馬の情報と、そのレースにおける結果を管理する

| カラム名 | MySQL型 |  NULL | 制約 | 説明 |  
|---|---|---|---|---|  
| id | BIGINT |  NO | PK, AUTO_INCREMENT | 出走情報を識別するID |  
| race_id | BIGINT | NO | FK | レースID |  
| horse_id | BIGINT | NO | FK | 競走馬ID |  
| frame_number | INT | NO |  | 枠番 |  
| horse_number | INT | NO |  | 馬番 |  
| finished | BOOLEAN | NO |  | 完走したか |  
| finish_position | INT | YES |  | 着順 |  
| time_seconds | DECIMAL(6,3) | YES |  | 走破タイム(秒) |  
| last_3f | DECIMAL(4,1) | YES |  | 上がり3F |  
| running_style | VARCHAR(20) | YES |  | そのレースでの脚質 |

### 設計方針
- `race_id`は`races.id`を参照する外部キーとする。
- `horse_id`は`horses.id`を参照する外部キーとする。
- `race_id`と`horse_id`の組み合わせにUNIQUE制約を設定する。
- 同じレースに同じ競走馬を複数登録できないようにする。
- `finished`によって完走したかどうかを管理する。
- `finished`が`false`の場合、`finish_position`はNULLを許可する。
- 未完走の場合、着順が存在しないため、無理に値を設定しない。
- `time_seconds`は走破タイムを秒単位の数値として保存する。
- 数値として保存することで、平均タイムなどの集計を容易にする。
- `last_3f`は上がり3Fのタイムを数値として保存する。
- `running_style`は馬に固定された属性ではなく、そのレースにおける脚質として管理する。
- `time_seconds`、`last_3f`、`running_style`は、取得できない場合を考慮してNULLを許可する。

## 6.テーブル間のリレーション

### 6.1 horses - race_entries
`horses`と`race_entries`は1対多の関係とする。  
1頭の競走馬は、複数のレースに出走することができる。  
```  
horses 1 ─── N race_entries  
```  
- `horses.id`を`race_entries.horse_id`から参照する。
- 1頭の馬に対して、過去の複数レースの出走履歴を保持する。
- 同じ馬が異なる日付・異なるレースに出走した場合、それぞれ別の`race_entries`として登録する。

### 6.2 races - race_entries
`races`と`race_entries`は1対多の関係とする。  
1つのレースには複数の競走馬が出走する。  
```  
races 1 ─── N race_entries  
```  
- `races.id`を`race_entries.race_id`から参照する。
- 1つのレースに対して、出走した馬の数だけ`race_entries`を登録する。
- 同じレースに同じ馬を複数登録することはできない、

### 6.3 horses - races
'horses'と'races'は、'race_entries'を介した多対多の関係となる。  
```  
horses N ─── M races  
　　　\　　　　　/  
　　　race_entries  
```  
1頭の馬は複数のレースに出走でき、1つのレースには複数の馬が出走する。  
`race_entries`には、その馬がそのレースで記録した結果や脚質などの情報を保持する。

## 7. 制約・設計ルール

### 7.1 出走情報の重複防止
`race_entries`の`race_id`と`horse_id`の組み合わせにUNIQUE制約を設定する。  
同じレースに同じ競走馬を複数登録することを防止する。  
```
UNIQUE(race_id, horse_id)
```  
同じ競走馬が異なるレースに出走した場合は、別の`race_entries`として登録する。

### 7.2 完走・着順の扱い
`finished`によって、そのレースを完走したかどうかを管理する。  
完走した場合は`finish_position`に着順を保存する。  
```  
finished = true  
finish_position = 1  
```  
未完走の場合は着順が存在しないため、`finish_position`をNULLとする。  
```  
finished = false  
finish_position = NULL  
```

### 7.3 分析対象となる過去レース数
適正分析を行う際は、対象馬に最低3レース以上の過去レースデータが存在することを条件とする。  
```  
3レース以上 → 分析対象  
2レース以下 → 分析対象外  
```  
データ不足の馬を低い適正スコアとして扱うのではなく、「分析対象外」「データ不足」として別途表示する。

### 7.4 脚質の扱い
脚質は競走馬に固定された属性として扱わず、`race_entries`にレースごとの情報として保存する。  
過去の`race_entries`を集計することで、競走馬の脚質傾向を分析する。  
```  
先行  
先行  
差し  
先行  
差し  
```  
↓  
```  
先行 3回  
差し 2回  
```  
このような集計結果から、その馬の脚質傾向を判断する。

### 7.5 走破タイムの扱い
走破タイムは文字列ではなく、秒単位の数値として`time_seconds`に保存する。  
これにより、平均タイムや条件別のタイム集計などの数値分析を容易にする。

### 7.6 NULLの扱い
`time_seconds`、`last_3f`、`running_style`など、取得できない可能性がある情報はNULLを許可する。  
存在しない情報を無理にデフォルト値で埋めず、「データが存在しない」ことをNULLで表現する。

### 7.7 データベースの責務
MySQLを正規データの保存先とする。  
馬・レース・出走結果などの原本データをMySQLで管理する。  
Spring Batchによる集計処理やElasticsearchへの検索用データ作成は、MySQLに保存されたデータを基準として行う。

