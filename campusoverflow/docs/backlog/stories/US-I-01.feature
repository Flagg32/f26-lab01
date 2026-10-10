Feature: 学号注册
  作为本校学生
  我希望用学号与邮箱注册账号
  以便获得带有校园身份标识的账号

  Scenario: 学生使用合法学号成功注册
    Given 我在注册页面
    When 我填写学号"20220101"
    And 我填写姓名"张三"
    And 我填写邮箱"zhangsan@hbuas.edu.cn"
    And 我填写密码"Passw0rd!"
    And 我点击注册
    Then 我的账号应被创建且状态为"待邮箱验证"
    And 我应收到一封验证邮件
    And 我不应能在此状态下发布问题

  Scenario Outline: 学号格式校验
    Given 我在注册页面
    When 我填写学号为 <学号>
    And 我填写其余合法信息
    And 我点击注册
    Then 结果应为 <结果>
    Examples:
      | 学号       | 结果                      |
      | 20220101   | 注册成功                  |
      | 2022010    | 提示"学号应为 8 位数字"   |
      | 202201010  | 提示"学号应为 8 位数字"   |
      | abcdefgh   | 提示"学号应为 8 位数字"   |

  Scenario: 学号已被注册
    Given 学号"20220101"已被注册
    And 我在注册页面
    When 我用同一学号提交注册
    Then 页面应提示"该学号已被注册"
    And 我不应收到验证邮件
